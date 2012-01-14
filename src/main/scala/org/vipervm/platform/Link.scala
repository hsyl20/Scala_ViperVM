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

package org.vipervm.platform

/**
 * A link between two memory nodes
 */
abstract class Link {
  /** Network this link is part of */
  val network:Network

  /** Source of the link */
  val source:MemoryNode

  /** Target of the link */
  val target:MemoryNode

  /** Start a copy using this link */
  def copy(source:BufferView, target:BufferView):DataTransfer =
    network.memoryCopier.copy(this, source, target)
}
