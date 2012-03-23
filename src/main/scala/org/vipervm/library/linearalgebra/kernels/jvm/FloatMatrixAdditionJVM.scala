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

package org.vipervm.library.linearalgebra.kernels.jvm

import org.vipervm.library.linearalgebra.kernels.prototypes._
import org.vipervm.platform.jvm._

object FloatMatrixAdditionJVMKernel extends JVMKernel with FloatMatrixAdditionPrototype {

  def fun(params:Seq[Any]): Unit = {
    val (w,h) = (params(width), params(height))
    val (m1,m2,m3) = (params(a).peer,params(b).peer,params(c).peer)

    var i = 0L
    while (i < h) {
      var j = 0L
      while (j < w) {
        val pos = (i*w+j)*4
        m3.setFloat(pos,m1.getFloat(pos) + m2.getFloat(pos))
        j += 1
      }
      i += 1
    }
  }
}
