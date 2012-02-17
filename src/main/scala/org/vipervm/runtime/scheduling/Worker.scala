/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.runtime.scheduling

import grizzled.slf4j.Logging
import org.vipervm.platform.{Processor,UserEvent,FutureEvent,EventGroup,Data}
import org.vipervm.runtime._
import org.vipervm.runtime.scheduling.Messages._

import org.vipervm.utils._

import scala.actors.Actor

/**
 * There is one worker per device.
 */
class Worker(val proc:Processor, scheduler:Scheduler, id:Int) extends Actor with Logging {

  private var tasks:List[Task] = Nil
  private var currentTask:Option[Task] = None

  private val memory = proc.memory

  private var datas:Map[Data,DataState] = Map.empty

  def dataState(data:Data):DataState = (this !? QueryDataState(data)).asInstanceOf[DataState]

  start

  def act:Unit = loop { react {

    case QueryDataState(data) => {
      sender ! datas.getOrElse(data, DataUnavailable)
    }

    case ExecuteTask(task) => {

      if (currentTask.isDefined) {
        tasks ::= task
      }
      else {
        executeTask(task)
      }

    }

    case TaskComplete(task) => {
      assert(task == currentTask.get)
      
      info("[Worker %d] Task complete: %s".format(id,task))

      /* Notify scheduler */
      scheduler ! TaskComplete(task)

      /* Execute another task, if any */
      currentTask = None
      tasks match {
        case t :: l => {
          tasks = l
          executeTask(t)
        }
        case Nil => ()
      }
    }
  }}

  private def executeTask(task:Task):Unit = {
    currentTask = Some(task)

    info("[Worker %s] Execute task: %s".format(this,task))

    /* Select kernel */
    val kernel = task.kernel.peer match {
      case k:MetaKernel => k.getKernelsFor(proc).head
      case k => k
    }

    /* Some parameters must be allocated in device memory whilst some other
       must be allocated in host memory. It depends on the kernel selected */
    val (hostParams,deviceParams) = task.kernel.paramsPerStorage(task.params)

    val futureViews = deviceParams.map(data => data.viewIn(memory) match {
      case Some(v) => FutureEvent(v)
      case None => {
        /* Allocate required buffers and views */
        //FIXME: support "no space left on device" exception
        val view = data.allocate(memory)

        /* Test if the view is read or written into */
       if (data.isDefined) {

          /* Schedule required data transfer to update the view */
          val sources = data.views

          /* Select source */
          val source = sources.head._2

          /* Select link */
          //FIXME: We need to support multi-hop links
          val link = scheduler.platform.linkBetween(source,view).get
          
          val transfer = link.copy(source,view)

          /* Schedule data-view association */
          val assocEvent = new UserEvent
          transfer.willTrigger {
            data.store(view)
            assocEvent.complete
          }

          FutureEvent(view, assocEvent)
        }
        else {
          data.store(view)
          FutureEvent(view)
        }
      }
    })
    
    new EventGroup(futureViews).willTrigger {

      /* Schedule kernel execution */
      val ev = proc.execute(kernel, task.makeKernelParams(memory))

      /* Schedule notification after kernel execution */
      ev.willTrigger {
        this ! TaskComplete(task)
      }
    }
  }

  def canExecute(task:Task):Boolean = task.canExecuteOn(proc)
}

