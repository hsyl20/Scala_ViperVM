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

import org.vipervm.platform.{BufferView2D,MemoryNode}
import org.vipervm.runtime.Data

/**
 * Strided 2D raw data
 */
class Strided2DRawData(width:Long,height:Long) extends Data {
  type ViewType = BufferView2D

  def allocate(memory:MemoryNode):BufferView2D = {
    //TODO: manage padding correctly
    val buffer = memory.allocate(width*height)
    new BufferView2D(buffer, 0, width, height, 0)
  }
}
