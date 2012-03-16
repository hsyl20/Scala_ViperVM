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

package org.vipervm.platform.host

import org.vipervm.platform._

class DefaultHostProcessor(mem:DefaultHostMemoryNode) extends HostProcessor {

  type MemoryNodeType = DefaultHostMemoryNode

  val memories = List(mem)

  def compile(kernel:Kernel):Unit = {}

  def execute(kernel:Kernel, args:Seq[Any]): KernelExecution = {
    throw new Exception("Unable to execute kernels (not implemented)")
  }

  override def toString:String = "Host: Default Host Driver - %d cores".format(
    Runtime.getRuntime.availableProcessors
  )

}
