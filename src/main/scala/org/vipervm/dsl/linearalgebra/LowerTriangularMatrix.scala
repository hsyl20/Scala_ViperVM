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

class LowerTriangularMatrix[A] {
  /**
   * Drop some columns (on the left)
   */
  def dropColumn(n:Int): LowerTriangularMatrix[A] = sys.error("undefined")

  /**
   * Take some columns (on the left)
   */
  def takeColumn(n:Int): List[Vector[A]] = sys.error("undefined")

  /**
   * Take the first column
   */
  def firstColumn: Vector[A] = sys.error("undefined")

  /**
   * Return an even blocking of the matrix
   */
  def blocking: LowerTriangularMatrix[Matrix[A]] = sys.error("undefined")

  /**
   * Apply f to every element of the matrix
   */
  def map[B](f:A=>B): LowerTriangularMatrix[B] = sys.error("undefined")

  /**
   * Zip this matrix with another
   */
  def zip[B](m:LowerTriangularMatrix[B]): LowerTriangularMatrix[(A,B)] = sys.error("undefined")

  /**
   * Concat a column on the left
   */
  def ::(v:Vector[A]): LowerTriangularMatrix[A] = sys.error("undefined")

  /**
   * Full matrix
   */
  def toMatrix: Matrix[A] = sys.error("udnefined")
}

