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

import org.vipervm.utils._

class DoubleValue(platform:Platform) extends Value[Double] {
  type ViewType = BufferView1D

  def allocate(memory:MemoryNode):BufferView1D = {
    val buffer = memory.allocate(8)
    new BufferView1D(buffer, 0, 8)
  }

  /**
   * Read value that *must* be in host memory
   */
  def value:Double = {
    val view = viewIn(platform.hostMemory).get
    val buf = view.buffer.asInstanceOf[HostBuffer].peer
    buf.getDouble(view.offset)
  }
}
