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

import org.vipervm.platform.{BufferView1D,MemoryNode}
import org.vipervm.runtime.Data

/**
 * Contiguous raw data
 */
class RawData(size:Long) extends Data {
  type ViewType = BufferView1D

  def allocate(memory:MemoryNode):BufferView1D = {
    val buffer = memory.allocate(size)
    new BufferView1D(buffer, 0, size)
  }
}