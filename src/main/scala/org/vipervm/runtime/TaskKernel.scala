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

/**
 * Indicate how to convert task parameters to kernel parameters for a given kernel
 */
abstract class TaskKernel extends Prototyped {

  val peer:Kernel

  def makeKernelParams(params:Seq[Data],memory:MemoryNode):Seq[Any]

  def canExecuteOn(proc:Processor) = peer.canExecuteOn(proc)

  def memoryConfig(params:Seq[Data],memory:MemoryNode,hostMemory:MemoryNode):Seq[(Data,MemoryNode)] = {
    if (params.length != prototype.length) throw new Exception("Invalid parameters")
    params zip prototype.map(_.storage match {
      case HostStorage => hostMemory
      case DeviceStorage => memory
    })
  }
}
