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

import scala.actors.{Actor,OutputChannel}

import org.vipervm.platform._
import org.vipervm.runtime._

abstract class DefaultScheduler extends Scheduler {
  def act = loop {
    react {
      reactions
    }
  }

  protected def reactions:PartialFunction[Any,Unit] = {
    case SubmitTask(task,deps) => sender ! submitTask(task,deps)
    case TransferComplete(transfer) => transferComplete(transfer)
    case TaskComplete(task) => taskComplete(task)
    case DiscardData(data) => discardData(data)
    case RetrieveData(data) => retrieveData(sender, data)
  }

  protected def submitTask(task:Task,deps:Seq[Event]):Event
  protected def transferComplete(transfer:DataTransfer):Unit
  protected def taskComplete(task:Task):Unit
  protected def discardData(data:Data):Unit
  protected def retrieveData(sender:OutputChannel[_],data:Data):Unit
}
