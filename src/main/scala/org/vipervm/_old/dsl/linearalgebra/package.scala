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

  implicit def wrappedInt[A<:AnyVal](v:A) = new Value[A](v)

  implicit def wrappedNum[A](e:Expr[Num[A]]) = new WrappedNum[A](e)
  implicit def wrappedMN[A](e:Expr[Matrix[Num[A]]]) = new WrappedMN[A](e)

  implicit def wrappedM[A](e:Expr[Matrix[A]]) = new WrappedM[A](e)
  implicit def wrappedLTM[A](e:Expr[LowerTriangularMatrix[A]]) = new WrappedLTM[A](e)
  implicit def wrappedLTMM[A](e:Expr[LowerTriangularMatrix[Matrix[A]]]) = new WrappedLTMM[A](e)
  implicit def wrappedMM[A](e:Expr[Matrix[Matrix[A]]]) = new WrappedMM[A](e)
  implicit def wrappedV[A](e:Expr[Vector[A]]) = new WrappedV[A](e)
  implicit def wrappedVM[A](e:Expr[Vector[Matrix[A]]]) = new WrappedVM[A](e)

  implicit def wrappedTuple2[A,B](e:Expr[(A,B)]) = new WrappedTuple2(e)

  //implicit def wrappedFun1[A,B](f:Expr[A]=>Expr[B]) = new ExprFun1(f)

  

}
