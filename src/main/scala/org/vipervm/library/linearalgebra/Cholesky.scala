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

class Cholesky {

  def cholesky[A](m:Expr[LowerTriangularMatrix[Num[A]]]): Expr[LowerTriangularMatrix[Num[A]]] = {
    cholesky_dp(m) | cholesky_blocking(m)
  }

  /**
   * Data-parallel Cholesky 
   */
  def cholesky_dp[A](m:Expr[LowerTriangularMatrix[Num[A]]]): Expr[LowerTriangularMatrix[Num[A]]] = {
    val (a11,a21) = m.firstColumn.split(1).toTuple
    val a22 = m.dropColumn(1)
    val l11 = a11.first.sqrt
    val l21 = a21.map(x => x/l11)
    val l22b = (l21 × l21).map(a => a._1 * a._2).lowerTriangular.zip(a22).map(a => a._2 - a._1)
    //val l22 = cholesky(l22b)
    val l22 = l22b //FIXME

    (l11 :: l21) :: l22
  }

  def cholesky_blocking[A](m:Expr[LowerTriangularMatrix[Num[A]]]): Expr[LowerTriangularMatrix[Num[A]]] = {
    val mb = m.blocking
    cholesky_blocked(mb)
  }

  /**
   * Cholesky using blocks
   */
  def cholesky_blocked[A](mb:Expr[LowerTriangularMatrix[Matrix[Num[A]]]]): Expr[LowerTriangularMatrix[Num[A]]] = {
    val (a11,a21) = mb.firstColumn.split(1).toTuple
    val a22 = mb.dropColumn(1)
    //val l11 = cholesky(a11.first.lowerTriangular)
    val l11 = a11.first.lowerTriangular //FIXME
    val l21 = a21.map(x => Blas.trsm(l11,x))
    val l22b = (l21 × l21).map(a => a._1 * a._2).lowerTriangular.zip(a22).map(a => a._2 - a._1)

    //val l22 = cholesky_blocked(l22b) | cholesky_fusion(l22b)
    val l22 = l22b.flatten//FIXME

    ((l11.toMatrix :: l21) :: l22.blocking).flatten
  }

  def cholesky_fusion[A](mb:Expr[LowerTriangularMatrix[Matrix[Num[A]]]]): Expr[LowerTriangularMatrix[Num[A]]] = {
      cholesky(mb.flatten)
  }
}
