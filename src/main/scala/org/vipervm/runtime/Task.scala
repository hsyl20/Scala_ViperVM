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
 * A functional kernel with its parameters
 */
case class Task(kernel:MetaKernel, params:Seq[Data], result:Data) {

  def makeKernelParams(memory:MemoryNode):Seq[Any] = kernel.makeKernelParams(params, memory)

  def canExecuteOn(proc:Processor):Boolean = kernel.canExecuteOn(proc)

  override def toString = "%s <- %s(%s)".format(result,kernel,params.mkString(","))
}
