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
import org.vipervm.platform.Processor
import org.vipervm.runtime._
import scala.actors.Actor
import org.vipervm.utils._
import scala.concurrent.Lock

/**
 * There is one worker per device.
 */
class Worker(val proc:Processor, scheduler:Scheduler) extends Actor with Logging {

  private var tasks:List[Task] = Nil
  private var currentTask:Option[Task] = None

  private val memory = proc.memory

  start

  def act:Unit = loop { react {

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
      
      info("[Worker %s] Task complete: %s".format(this,task))

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

    /* Schedule required data transfers */
    task.params.foreach { _ match {
      case DataValue(data) => {
        val view = data.viewIn(memory).getOrElse(data.allocateStore(memory))
        //TODO: update view with valid contents
        //TODO: check size
      }
      case _ => ()
    }}

    /* Select kernel */
    val kernel = task.kernel.peer match {
      case k:MetaKernel => k.getKernelsFor(proc).head
      case k => k
    }

    /* Schedule kernel execution */
    val ev = proc.execute(kernel, task.makeKernelParams(memory))

    /* Schedule notification after kernel execution */
    ev.willTrigger {
      this ! TaskComplete(task)
    }
  }

  def canExecute(task:Task):Boolean = task.canExecuteOn(proc)
}

