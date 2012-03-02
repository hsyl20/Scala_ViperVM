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

object FloatMatrixMultiplicationJVM extends JVMKernel with FloatMatrixMultiplicationPrototype {

  def fun(params:Seq[Any]): Unit = {
    val (wA,hA,wB) = (params(widthA), params(heightA), params(widthB))
    val (m1,m2,m3) = (params(a).peer,params(b).peer,params(c).peer)

    var i = 0L
    while (i < hA) {
      var j = 0L
      while (j < wB) {
        var k = 0L
        var sum = 0.0f
        while (k < wA) {
          sum += m1.getFloat((i*wA+k)*4) * m2.getFloat((k*wB+j)*4)
          k += 1
        }
        m3.setFloat((i*wB+j)*4, sum)
        j += 1
      }
      i += 1
    }
  }
}

