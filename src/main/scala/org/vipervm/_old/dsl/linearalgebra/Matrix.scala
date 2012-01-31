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

class Matrix[A]

class WrappedM[A](expr:Expr[Matrix[A]]) {

  /**
   * Drop some columns (on the left)
   */
  def dropColumns(n:Expr[Int]) = MethodCall[Matrix[A]](expr, "dropColumns", n)

  /**
   * Take some columns (on the left)
   */
  def takeColumns(n:Expr[Int]) = MethodCall[Matrix[A]](expr, "takeColumns", n)

  /**
   * Take the first column
   */
  def firstColumn = MethodCall[Vector[A]](expr, "firstColumn")

  /**
   * Return an even blocking of the matrix
   */
  def blocking = MethodCall[Matrix[Matrix[A]]](expr, "blocking")

  /**
   * Apply f to every element of the matrix
   */
  def map[B](f:ExprFun1[A,B]) = MethodCall[Matrix[B]](expr, "map", f)
  def map[B](f:Expr[A]=>Expr[B]) = MethodCall[Matrix[B]](expr, "map", ExprFun1(f))

  /**
   * Zip this matrix with another
   */
  def zip[B](m:Expr[Matrix[B]]) = MethodCall[Matrix[(A,B)]](expr, "zip", m)

  /**
   * Extract the lower-triangular matrix
   */
  def lowerTriangular = MethodCall[LowerTriangularMatrix[A]](expr, "lowerTriangular")

  /**
   * Concat a column on the left
   */
  def ::(v:Expr[Vector[A]]) = MethodCall[Matrix[A]](expr, "::", v)

  /**
   * Concat a matrix on the left
   */
  def :::(v:Expr[Matrix[A]]) = MethodCall[Matrix[A]](expr, ":::", v)
}
