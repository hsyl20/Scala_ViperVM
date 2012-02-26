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

package org.vipervm.runtime

import org.vipervm.platform._
import org.vipervm.runtime._

/**
 * A kernel that can be executed on different architectures
 *
 * Kernels must all take the same parameters as input (same prototype)
 */
trait MetaKernel extends Prototyped {

  /**
   * Get kernels for the given processor
   */
  def getKernelsFor(proc:Processor): Seq[Kernel]

  def makeKernelParams(params:Seq[Data],memory:MemoryNode):Seq[Any]

  def canExecuteOn(proc:Processor) = !getKernelsFor(proc).isEmpty

  def memoryConfig(params:Seq[Data],memory:MemoryNode,hostMemory:MemoryNode):Seq[(Data,MemoryNode)] = {
    if (params.length != prototype.length) throw new Exception("Invalid parameters")
    params zip prototype.map(_.storage match {
      case HostStorage => hostMemory
      case DeviceStorage => memory
    })
  }

}
