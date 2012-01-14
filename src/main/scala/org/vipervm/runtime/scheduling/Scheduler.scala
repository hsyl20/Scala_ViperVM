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

trait Scheduler extends Actor

abstract class SchedulerMessage
case class SubmitTask(task:Task,deps:Seq[Event]) extends SchedulerMessage
case class TransferComplete(transfer:DataTransfer) extends SchedulerMessage
case class TaskComplete(task:Task) extends SchedulerMessage
case class DiscardData(data:Data) extends SchedulerMessage
case class RetrieveData(data:Data) extends SchedulerMessage
