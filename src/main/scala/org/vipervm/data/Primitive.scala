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

/**
 * Primitive type description
 */
trait Primitive[T] {

  /** Size in bytes */
  def size:Int

}


object Primitive {
  implicit val primitiveFloat = new Primitive[Float] {
    def size = 4
  }

  implicit val primitiveInt = new Primitive[Int] {
    def size = 4
  }

  implicit val primitiveLong = new Primitive[Long] {
    def size = 8
  }
}
