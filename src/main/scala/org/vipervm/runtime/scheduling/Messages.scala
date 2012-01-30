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

import org.vipervm.runtime.{Task,Data}
import org.vipervm.platform.{Event,DataTransfer}

case class SubmitTask(task:Task,deps:Seq[Event])
case class TransferComplete(transfer:DataTransfer)
case class DiscardData(data:Data)
case class RetrieveData(data:Data)
case class ExecuteTask(task:Task)
case class TaskComplete(task:Task)
