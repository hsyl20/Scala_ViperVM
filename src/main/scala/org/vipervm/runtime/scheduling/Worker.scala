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

import org.vipervm.platform.{Processor,UserEvent,FutureEvent,EventGroup,Data}
import org.vipervm.runtime._
import org.vipervm.runtime.scheduling.Messages._

import org.vipervm.profiling._

import org.vipervm.utils._

import scala.actors.Actor

/**
 * There is one worker per device.
 */
class Worker(val proc:Processor, scheduler:Scheduler, profiler:Profiler, dataManager:DataManager) extends Actor {

  private var tasks:List[Task] = Nil
  private var currentTask:Option[Task] = None

  private val memory = proc.memory

  private var datas:Map[Data,DataState] = Map.empty

  def dataState(data:Data):DataState = (this !? QueryDataState(data)).asInstanceOf[DataState]

  def loadStatus:LoadStatus = (this !? QueryLoadStatus).asInstanceOf[LoadStatus]

  start

  def act:Unit = loop { react {

    case QueryDataState(data) => {
      sender ! datas.getOrElse(data, DataUnavailable)
    }

    case QueryLoadStatus => {
      sender ! LoadStatus(tasks.length + (if (currentTask.isDefined) 1 else 0))
    }

    case ExecuteTask(task) => {
      profiler ! TaskAssigned(task,proc)

      if (currentTask.isDefined) {
        tasks ::= task
      }
      else {
        executeTask(task)
      }

    }

    case TaskComplete(task) => {
      assert(task == currentTask.get)
      
      profiler ! TaskCompleted(task,proc)

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


    /* Select kernel */
    val kernel = task.kernel.peer match {
      case k:MetaKernel => k.getKernelsFor(proc).head
      case k => k
    }

    val memConf = task.kernel.memoryConfig(task.params,memory,scheduler.platform.hostMemory)

    val event = dataManager.prepare(memConf)

    event.willTrigger {

      /* Schedule kernel execution */
      val ev = proc.execute(kernel, task.makeKernelParams(memory))

      profiler ! TaskStart(task,kernel,proc)

      /* Schedule notification after kernel execution */
      ev.willTrigger {
        this ! TaskComplete(task)

        dataManager.release(memConf)
      }
    }
  }

  def canExecute(task:Task):Boolean = task.canExecuteOn(proc)
}

