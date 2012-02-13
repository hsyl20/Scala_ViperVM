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

import org.vipervm.runtime.data.Primitives._
import org.vipervm.platform.{BufferView2D,MemoryNode}
import org.vipervm.runtime.Data

/**
 * 2D matrix
 */
class Matrix2D[A](val width:Long, val height:Long)(implicit elem:Primitive[A]) extends Data {
  type ViewType = BufferView2D

  def allocate(memory:MemoryNode):BufferView2D = {
    //TODO: manage padding correctly
    val buffer = memory.allocate(elem.size*width*height)
    new BufferView2D(buffer, 0, elem.size*width, height, 0)
  }
}

object Matrix2D {
  def filled[A : Primitive](width:Long, height:Long, value:A) = {
    val m = new Matrix2D[A](width,height)

  }
}
