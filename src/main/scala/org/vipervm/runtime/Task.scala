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

/**
 * A functional kernel with its parameters
 */
case class Task(kernel:TaskKernel, params:Seq[Value]) {

  def makeKernelParams(memory:MemoryNode):Seq[KernelParameter] = kernel.makeKernelParams(params, memory)

  def canExecuteOn(proc:Processor):Boolean = kernel.canExecuteOn(proc)
}
