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

object DefaultHostDriver extends HostDriver {
  private val mem = new DefaultHostMemoryNode
  private val proc = new DefaultHostProcessor(mem)

  def processors:Seq[Processor] = Seq(proc)

  def networks:Seq[Network] = Nil

  def memories:Seq[HostMemoryNode] = Seq(mem)

  def shutdown:Unit = {}
}
