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

package org.vipervm.dsl.linearalgebra


class LowerTriangularMatrix[A]

object LowerTriangularMatrix {
  def apply[A](width:Expr[Long]) = FunCall[LowerTriangularMatrix[A]]("LowerTriangularMatrix.empty", width)
}

class WrappedLTM[A](expr:Expr[LowerTriangularMatrix[A]]) {
  /**
   * Drop some columns (on the left)
   */
  def dropColumn(n:Expr[Int]) = MethodCall[LowerTriangularMatrix[A]](expr, "dropColumn", n)

  /**
   * Take the first column
   */
  def firstColumn = MethodCall[Vector[A]](expr, "firstColumn")

  /**
   * Return an even blocking of the matrix
   */
  def blocking = MethodCall[LowerTriangularMatrix[Matrix[A]]](expr, "blocking")

  /**
   * Apply f to every element of the matrix
   */
  def map[B](f:Expr[A]=>Expr[B]) = MethodCall[LowerTriangularMatrix[B]](expr, "map", ExprFun1(f))
  def map[B](f:ExprFun1[A,B]) = MethodCall[LowerTriangularMatrix[B]](expr, "map", f)

  /**
   * Zip this matrix with another
   */
  def zip[B](m:Expr[LowerTriangularMatrix[B]]) = MethodCall[LowerTriangularMatrix[(A,B)]](expr, "zip", m)

  /**
   * Concat a column on the left
   */
  def ::(v:Expr[Vector[A]]) = MethodCall[LowerTriangularMatrix[A]](expr, "::", v)

  /**
   * Full matrix
   */
  def toMatrix = MethodCall[Matrix[A]](expr, "toMatrix")
}

class WrappedLTMM[A](expr:Expr[LowerTriangularMatrix[Matrix[A]]]) {
  /**
   * Flatten blocks of the matrix
   */
  def flatten = MethodCall[LowerTriangularMatrix[A]](expr, "flatten")
}

class WrappedMM[A](expr:Expr[Matrix[Matrix[A]]]) {
  /**
   * Flatten blocks of the matrix
   */
  def flatten = MethodCall[Matrix[A]](expr, "flatten")
}
