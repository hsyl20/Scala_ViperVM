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

class DefaultHostDriver extends HostDriver {
  private val mem = new DefaultHostMemoryNode
  private val proc = new DefaultHostProcessor(mem)

  def processors:Seq[Processor] = Seq(proc)

  def networks:Seq[Network] = Nil

  def memories:Seq[HostMemoryNode] = Seq(mem)
}
