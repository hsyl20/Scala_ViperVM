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

package org.vipervm.runtime.data

import org.vipervm.platform._

/**
 * Contiguous raw data
 */
class RawData(size:Long) extends MetaView {
  type ViewType = BufferView1D

  def allocate(memory:MemoryNode):BufferView1D = {
    val buffer = memory.allocate(size)
    new BufferView1D(buffer, 0, size)
  }
}
