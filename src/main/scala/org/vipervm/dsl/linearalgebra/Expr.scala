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

sealed abstract class Expr[A] {
  def | (e:Expr[A]) = Choice(this,e)
}

case class FunCall[A](name:String, args:Expr[_]*) extends Expr[A]
case class ClosureCall[A,B](f:ExprFun1[A,B],a:Expr[A]) extends Expr[B]
case class MethodCall[A](self:Expr[_], name:String, args:Expr[_]*) extends Expr[A]
case class Value[A](v:AnyVal) extends Expr[A]

case class ExprFun1[A,B](f:Expr[A]=>Expr[B]) extends Expr[A=>B] {
  def call(a:Expr[A]) = ClosureCall[A,B](this,a)
}

case class Choice[A](cs:Expr[A]*) extends Expr[A] 

case class Dummy[A](id:Int = 0) extends Expr[A]
