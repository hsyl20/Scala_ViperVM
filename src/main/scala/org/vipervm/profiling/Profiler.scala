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

trait Profiler {

  def transferStart(data:MetaView,dataTransfer:DataTransfer,timestamp:Long = System.nanoTime):Unit
  def transferEnd(data:MetaView,dataTransfer:DataTransfer,timestamp:Long = System.nanoTime):Unit
  def taskAssigned(task:Task,proc:Processor,timestamp:Long = System.nanoTime):Unit
  def taskStart(task:Task,kernel:Kernel,proc:Processor,timestamp:Long = System.nanoTime):Unit
  def taskCompleted(task:Task,proc:Processor,timestamp:Long = System.nanoTime):Unit

}
