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

import scala.actors.OutputChannel

import org.vipervm.platform._
import org.vipervm.runtime._

/**
 * "Offloading" scheduler
 *
 * This scheduler transfers inputs data, executes the kernel and transfers
 * output data back.
 */
/*class OffloadingScheduler(proc:Processor) extends DefaultScheduler {

  private var pendingTasks:List[Task] = Nil
  private var readyTasks:List[Task] = Nil
  private var taskEvent:Map[Task,Event] = Map.empty

  case class ReadyTask(task:Task)

  protected def submitTask(task:Task,deps:Seq[Event]):Event = {
    val ev = new UserEvent
    pendingTasks ::= task
    taskEvent += (task -> ev)

    val evg = new EventGroup(deps)
    evg.willTrigger {
      this ! ReadyTask(task)
    }
    
    ev
  }

  override def reactions = super.reactions orElse {
    case ReadyTask(task) => readyTask(task)
  }


  protected def readyTask(task:Task):Unit = { 
    pendingTasks.remove(_ == task)
    readyTasks ::= task

  }

  protected def transferComplete(transfer:DataTransfer):Unit
  protected def taskComplete(task:Task):Unit
  protected def discardData(data:Data):Unit
  protected def retrieveData(sender:OutputChannel[_],data:Data):Unit

}*/
