/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.platform.host

import fr.hsyl20.vipervm.platform._

class DefaultHostProcessor(mem:DefaultHostMemoryNode) extends Processor {
  val memories = Seq(mem)

  def execute(kernel:Kernel, args:Seq[KernelParameter]): KernelEvent = {
    throw new Exception("Unable to execute kernels (not implemented)")
  }
}
