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

import org.vipervm.platform.{Kernel,KernelParameter,MemoryNode,Processor}
import org.vipervm.runtime.interpreter.Value

/**
 * Indicate how to convert task parameters to kernel parameters for a given kernel
 */
abstract class TaskKernel {

  val peer:Kernel

  def makeKernelParams(params:Seq[Value],memory:MemoryNode):Seq[KernelParameter]

  def canExecuteOn(proc:Processor) = peer.canExecuteOn(proc)
}
