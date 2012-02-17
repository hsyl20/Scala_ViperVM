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

  /** Split params in two categories: params that need to be stored on host memory
      and params that need to be stored in device memory.
      Return a couple (hostParams,deviceParams) */
  def paramsPerStorage(params:Seq[Data]):(Seq[Data],Seq[Data]) = {
    if (params.length != prototype.length) throw new Exception("Invalid parameters")
    val sp = (params zip prototype.map(_.storage)).partition(_._2 == HostStorage)
    (sp._1.map(_._1), sp._2.map(_._1))
  }
}
