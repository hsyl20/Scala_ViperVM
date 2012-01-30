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
 * 2D matrix
 */
class Matrix2D(val elemSize:Int, val width:Long, val height:Long) extends Data {
  type ViewType = BufferView2D

  def allocate(memory:MemoryNode):BufferView2D = {
    //TODO: manage padding correctly
    val buffer = memory.allocate(elemSize*width*height)
    new BufferView2D(buffer, 0, elemSize*width, height, 0)
  }
}
