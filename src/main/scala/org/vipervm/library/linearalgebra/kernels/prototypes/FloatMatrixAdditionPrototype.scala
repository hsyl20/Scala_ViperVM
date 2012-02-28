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

trait FloatMatrixAdditionPrototype {
  val width = Parameter[Int](
    name = "width",
    mode = ReadOnly,
    storage = HostStorage,
    description = "Width of matrices"
  )
  val height = Parameter[Int](
    name = "height",
    mode = ReadOnly,
    storage = HostStorage,
    description = "Height of matrices"
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
    mode = ReadWrite,
    storage = DeviceStorage
  )

  val prototype = Prototype(width,height,a,b,c)

}
