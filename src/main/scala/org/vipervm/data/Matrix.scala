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

package org.vipervm.data

import org.vipervm.runtime.data.Matrix2D
import org.vipervm.runtime.ast._

object Matrix {
  import Primitive._

  /**
   * Generate a random matrix
   */
  def random[T](dims:Long*)(implicit prim:Primitive[T]): Term = {
    val ndims = dims.length
    ndims match {
      case 2 => DataTerm(new Matrix2D(prim.size, dims(0), dims(1)))
      case _ => throw new Exception("Not supported")
    }
  }
}
