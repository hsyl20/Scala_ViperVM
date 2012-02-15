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
  sealed abstract class Primitive[A] {
    val size:Int
    val typ:String
  }

  implicit case object FloatPrimitive extends Primitive[Float] {
    val size = 4
    val typ = "float"
  }

  implicit case object DoublePrimitive extends Primitive[Double] {
    val size = 8
    val typ = "double"
  }

  implicit case object IntPrimitive extends Primitive[Int] {
    val size = 4
    val typ = "int"
  }

  implicit case object LongPrimitive extends Primitive[Long] {
    val size = 8
    val typ = "long"
  }
}

