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

object Blas {
  /**
   * TRSM  solves one of the matrix equations
   * 
   * op( A )*X = alpha*B,   or   X*op( A ) = alpha*B,
   * where alpha is a scalar, X and B are m by n matrices, A is a unit, or
   * non-unit,  upper or lower triangular matrix  and  op( A )  is one  of
   *
   *     op( A ) = A   or   op( A ) = A**T.
   */
  def trsm[A](a:Expr[LowerTriangularMatrix[Num[A]]],b:Expr[Matrix[Num[A]]]) = FunCall[Matrix[Num[A]]]("trsm", a, b)
}
