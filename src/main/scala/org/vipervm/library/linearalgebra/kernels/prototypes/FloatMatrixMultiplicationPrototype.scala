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

package org.vipervm.library.linearalgebra.kernels.prototypes

import org.vipervm.platform._

trait FloatMatrixMultiplicationPrototype {
  val widthA = Parameter[Long](
    name = "widthA",
    mode = ReadOnly,
    storage = HostStorage,
    description = "Width of A"
  )
  val heightA = Parameter[Long](
    name = "widthA",
    mode = ReadOnly,
    storage = HostStorage,
    description = "Height of A"
  )
  val widthB = Parameter[Long](
    name = "widthB",
    mode = ReadOnly,
    storage = HostStorage,
    description = "Width of B"
  )
  val a = Parameter[Buffer](
    name = "a",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val b = Parameter[Buffer](
    name = "b",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val c = Parameter[Buffer](
    name = "c",
    mode = WriteOnly,
    storage = DeviceStorage
  )

  val prototype = Prototype(a,b,c,widthA,heightA,widthB)
}
