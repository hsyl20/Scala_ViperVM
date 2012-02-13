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

object Primitives {
  abstract class Primitive[A] {
    val size:Int
  }

  implicit object FloatPrimitive extends Primitive[Float] {
    val size = 4
  }

  implicit object DoublePrimitive extends Primitive[Double] {
    val size = 8
  }

  implicit object IntPrimitive extends Primitive[Int] {
    val size = 4
  }

  implicit object LongPrimitive extends Primitive[Long] {
    val size = 8
  }
}

