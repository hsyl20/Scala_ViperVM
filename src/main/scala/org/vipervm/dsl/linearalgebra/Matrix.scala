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

class Matrix[A] {

  /**
   * Drop some columns (on the left)
   */
  def dropColumn(n:Int): Matrix[A] = sys.error("undefined")

  /**
   * Take some columns (on the left)
   */
  def takeColumn(n:Int): Matrix[A] = sys.error("undefined")

  /**
   * Take the first column
   */
  def firstColumn: Vector[A] = sys.error("undefined")

  /**
   * Return an even blocking of the matrix
   */
  def blocking: Matrix[Matrix[A]] = sys.error("undefined")

  /**
   * Apply f to every element of the matrix
   */
  def map[B](f:A=>B): Matrix[B] = sys.error("undefined")

  /**
   * Zip this matrix with another
   */
  def zip[B](m:Matrix[B]): Matrix[(A,B)] = sys.error("undefined")

  /**
   * Extract the lower-triangular matrix
   */
  def lowerTriangular: LowerTriangularMatrix[A] = sys.error("undefined")

  /**
   * Concat a column on the left
   */
  def ::(v:Vector[A]): Matrix[A] = sys.error("undefined")

  /**
   * Concat a matrix on the left
   */
  def :::(v:Matrix[A]): Matrix[A] = sys.error("undefined")
}
