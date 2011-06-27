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

package org.vipervm.dsl

package object linearalgebra {
  implicit def ltmltm[A](m:LowerTriangularMatrix[Matrix[A]]) = new {
    /**
     * Flatten blocks of the matrix
     */
    def flatten:LowerTriangularMatrix[A] = sys.error("undefined")
  }

  implicit def mm[A](m:Matrix[Matrix[A]]) = new {
    /**
     * Flatten blocks of the matrix
     */
    def flatten:Matrix[A] = sys.error("undefined")
  }

  implicit def vm[A](m:Vector[Matrix[A]]) = new {
    /**
     * Flatten blocks of the matrix
     */
    def flatten:Matrix[A] = sys.error("undefined")
  }

  implicit def matadd[A<:Addable[A]](m:Matrix[A]) = new {
    def -(a:Matrix[A]):Matrix[A] = (m zip a) map (x => x._1 - x._2)
    def +(a:Matrix[A]):Matrix[A] = (m zip a) map (x => x._1 + x._2)
  }

  implicit def matmul[A <:Addable[A] with Multipliable[A]](m:Matrix[A]) = new {
    def *(a:Matrix[A]):Matrix[A] = sys.error("undefined")
  }

  implicit def num[A](num:A) = new {
    def sqrt:A = sys.error("undefined")
  }
}
