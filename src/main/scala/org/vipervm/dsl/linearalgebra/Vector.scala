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

class Vector[A]

class WrappedV[A](expr:Expr[Vector[A]]) {
  /**
   * Apply f to every element of the vector
   */
  def map[B](f:ExprFun1[A,B]) = MethodCall[Vector[B]](expr, "map", f)
  def map[B](f:Expr[A]=>Expr[B]) = MethodCall[Vector[B]](expr, "map", ExprFun1(f))

  /**
   * Split the vector in two parts, the first one contains n elements
   */
  def split(n:Expr[Int]) = MethodCall[(Vector[A],Vector[A])](expr, "split", n)

  /**
   * Concat a cell on top
   */
  def ::(v:Expr[A]) = MethodCall[Vector[A]](expr, "::", v)

  /**
   * Concat a vector on top
   */
  def :::(v:Expr[Vector[A]]) = MethodCall[Vector[A]](expr, ":::", v)

  /**
   * First element
   */
  def first = MethodCall[A](expr, "first")

  /**
   * Cartesian product
   */
  def ×[B](v:Expr[Vector[B]]) = MethodCall[Matrix[(A,B)]](expr, "×", v)
}

class WrappedVM[A](expr:Expr[Vector[Matrix[A]]]) {
  /**
   * Flatten blocks of the matrix
   */
  def flatten = MethodCall[Matrix[A]](expr, "flatten")
}
