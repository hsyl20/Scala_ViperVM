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
import org.vipervm.runtime.mm.Data

trait Profiler {

  def transferStart(dataTransfer:DataTransfer,timestamp:Long = System.nanoTime):Unit
  def transferEnd(dataTransfer:DataTransfer,timestamp:Long = System.nanoTime):Unit
  def taskAssigned(task:Task,proc:Processor,timestamp:Long = System.nanoTime):Unit
  def taskStart(task:Task,kernel:Kernel,proc:Processor,timestamp:Long = System.nanoTime):Unit
  def taskCompleted(task:Task,proc:Processor,timestamp:Long = System.nanoTime):Unit

}
