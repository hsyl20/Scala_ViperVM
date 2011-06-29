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

package org.vipervm.library.linearalgebra

import org.vipervm.dsl.linearalgebra._

class Cholesky[A] {

  val cholesky: ExprFun1[LowerTriangularMatrix[Num[A]],LowerTriangularMatrix[Num[A]]] = ExprFun1 { m =>
    cholesky_dp.call(m) | cholesky_blocking.call(m)
  }

  val cholesky_dp = ExprFun1[LowerTriangularMatrix[Num[A]],LowerTriangularMatrix[Num[A]]] { m => 
    val (a11,a21) = m.firstColumn.split(1).toTuple
    val a22 = m.dropColumn(1)
    val l11 = a11.first.sqrt
    val l21 = a21.map(x => x/l11)
    val l22b = (l21 × l21).map(a => a._1 * a._2).lowerTriangular.zip(a22).map(a => a._2 - a._1)
    val l22 = cholesky.call(l22b)

    (l11 :: l21) :: l22
  }

  val cholesky_blocking = ExprFun1[LowerTriangularMatrix[Num[A]],LowerTriangularMatrix[Num[A]]] { m =>
    val mb = m.blocking
    cholesky_blocked.call(mb)
  }

  val cholesky_blocked: ExprFun1[LowerTriangularMatrix[Matrix[Num[A]]],LowerTriangularMatrix[Num[A]]] = ExprFun1 { mb => 
    val (a11,a21) = mb.firstColumn.split(1).toTuple
    val a22 = mb.dropColumn(1)
    val l11 = cholesky.call(a11.first.lowerTriangular)
    val l21 = a21.map(x => Blas.trsm(l11,x))
    val l22b = (l21 × l21).map(a => a._1 * a._2).lowerTriangular.zip(a22).map(a => a._2 - a._1)

    val l22 = cholesky_blocked.call(l22b) | cholesky_fusion.call(l22b)

    ((l11.toMatrix :: l21) :: l22.blocking).flatten
  }

  val cholesky_fusion = ExprFun1[LowerTriangularMatrix[Matrix[Num[A]]],LowerTriangularMatrix[Num[A]]] { mb =>
      cholesky.call(mb.flatten)
  }
}
