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

  val memories = Seq(mem)

  def compile(kernel:Kernel):Unit = {}

  def execute(kernel:Kernel, args:Seq[KernelParameter]): KernelEvent = {
    throw new Exception("Unable to execute kernels (not implemented)")
  }
}
