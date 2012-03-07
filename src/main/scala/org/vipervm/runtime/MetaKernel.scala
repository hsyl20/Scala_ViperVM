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
import org.vipervm.runtime.mm.config._
import org.vipervm.runtime.mm.{Data,DataInstance}

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

  def makeKernelParams(params:Seq[DataInstance]):Seq[Any]

  def canExecuteOn(proc:Processor) = !getKernelsFor(proc).isEmpty

  def memoryConfig(params:Seq[Data],memory:MemoryNode,hostMemory:MemoryNode):DataConfig = {
    if (params.length != prototype.length) throw new Exception("Invalid parameters")
    val paramMem = params zip prototype.map(_.storage match {
      case HostStorage => hostMemory
      case DeviceStorage => memory
    })
    val constraints = paramMem map { case (p,m) => p requiredIn m }
    constraints.reduceLeft[DataConfig](_&&_)
  }

}
