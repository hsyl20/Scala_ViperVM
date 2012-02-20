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

package org.vipervm.profiling

import org.vipervm.platform._
import org.vipervm.runtime._

sealed abstract class ProfilingEvent {
  val timestamp = System.nanoTime
}

case class DataTransferStart(data:Data,dataTransfer:DataTransfer) extends ProfilingEvent
case class DataTransferEnd(data:Data,dataTransfer:DataTransfer) extends ProfilingEvent
case class TaskAssigned(task:Task,proc:Processor) extends ProfilingEvent
case class TaskStart(task:Task,kernel:Kernel,proc:Processor) extends ProfilingEvent
case class TaskCompleted(task:Task,proc:Processor) extends ProfilingEvent
