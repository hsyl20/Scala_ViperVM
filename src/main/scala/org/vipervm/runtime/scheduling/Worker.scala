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

import org.vipervm.platform.Processor
import org.vipervm.runtime._
import scala.actors.Actor
import org.vipervm.utils._
import scala.concurrent.Lock

/**
 * There is one worker per device.
 */
class Worker(val proc:Processor, scheduler:Scheduler) extends Actor {

  private val lock = new Lock
  private var tasks:List[Task] = Nil
  private var currentTask:Option[Task] = None

  private val memory = proc.memory

  start

  def act:Unit = loop { react {

    case ExecuteTask(task) => {
      lock.acquire

      if (currentTask.isDefined) {
	tasks ::= task
      }
      else {
	executeTask(task)
      }

      lock.release
    }

    case TaskComplete(task) => {
      assert(task == currentTask.get)
      
      /* Notify scheduler */
      scheduler ! TaskComplete(task)

      /* Execute another task, if any */
      lock.acquire

      currentTask = None
      tasks.headOption.foreach(executeTask)

      lock.release
    }
  }}

  private def executeTask(task:Task):Unit = {
    currentTask = Some(task)

    /* Schedule required data transfers */
    task.params.foreach { _ match {
      case DataTaskParameter(data) => {
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

