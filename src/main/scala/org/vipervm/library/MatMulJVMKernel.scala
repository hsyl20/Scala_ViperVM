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

package org.vipervm.library

import org.vipervm.platform.jvm._

object MatMulJVMKernel extends JVMKernel with MatMulKernelPrototype {

  def fun(params:Seq[Any]): Unit = {
    val w = params(n)
    val (m1,m2,m3) = (params(a).peer,params(b).peer,params(c).peer)

    var i = 0L
    while (i < w) {
      var j = 0L
      while (j < w) {
        var k = 0L
        var sum = 0.0f
        while (k < w) {
          sum += m1.getFloat((i*w+k)*4) * m2.getFloat((k*w+j)*4)
          k += 1
        }
        m3.setFloat((i*w+j)*4, sum)
        j += 1
      }
      i += 1
    }
  }
}

