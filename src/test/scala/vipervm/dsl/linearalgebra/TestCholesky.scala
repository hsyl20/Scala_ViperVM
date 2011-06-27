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

import org.scalatest.FunSuite

import org.vipervm.dsl.linearalgebra._

class TestDslLinearAlgebra extends FunSuite {

  test("Cholesky is typable") {
  }

  def cholesky[A <:Addable[A] with Multipliable[A] with Divisable[A]](m:LowerTriangularMatrix[A]): LowerTriangularMatrix[A] = {
    val (a11,a21) = m.firstColumn.split(1)
    val a22 = m.dropColumn(1)
    val l11 = a11.first.sqrt
    val l21 = a21.map(x => x/l11)
    val l22 = cholesky((l21 × l21).map(a => a._1 * a._2).lowerTriangular.zip(a22).map(a => a._2 - a._1))

    (l11 :: l21) :: l22
  }

  def cholesky_blocked[A<:Addable[A] with Multipliable[A] with Divisable[A]](m:LowerTriangularMatrix[A]): LowerTriangularMatrix[A] = {
    val mb = m.blocking
    val (a11,a21) = mb.firstColumn.split(1)
    val a22 = mb.dropColumn(1)
    val l11 = cholesky(a11.first.lowerTriangular)
    val l21 = a21.map(x => Blas.trsm(l11,x))
    val l22b = (l21 × l21).map(a => a._1 * a._2).lowerTriangular.zip(a22).map(a => a._2 - a._1)

    val l22 = cholesky(l22b.flatten).blocking

    ((l11.toMatrix :: l21) :: l22).flatten
  }
}
