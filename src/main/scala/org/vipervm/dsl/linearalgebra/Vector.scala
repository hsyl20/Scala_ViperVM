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

class Vector[A] {
  /**
   * Apply f to every element of the vector
   */
  def map[B](f:A=>B): Vector[B] = sys.error("undefined")

  /**
   * Split the vector in two parts, the first one contains n elements
   */
  def split(n:Int):(Vector[A],Vector[A]) = sys.error("undefined")

  /**
   * Concat a cell on top
   */
  def ::(v:A): Vector[A] = sys.error("undefined")

  /**
   * Concat a vector on top
   */
  def :::(v:Vector[A]): Vector[A] = sys.error("undefined")

  /**
   * First element
   */
  def first:A = sys.error("undefined")

  /**
   * Cartesian product
   */
  def ×[B](v:Vector[B]): Matrix[(A,B)] = sys.error("udnefined")
}
