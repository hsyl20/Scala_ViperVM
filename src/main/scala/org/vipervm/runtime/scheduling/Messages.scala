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

import org.vipervm.runtime.Task
import org.vipervm.platform.{Event,Data,DataTransfer}

object Messages {
  case class ExecuteTask(task:Task)
  case class TaskComplete(task:Task)
  case class SubmitTask(task:Task,deps:Seq[Event])
  case class TransferComplete(transfer:DataTransfer)
  case class QueryDataState(data:Data)

  case class DiscardData(data:Data)
  case class RetrieveData(data:Data)
}
