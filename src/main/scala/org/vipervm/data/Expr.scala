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

package org.vipervm.data


sealed abstract class Expr {
   def +(e:Expr) = Add(this, e)
   def -(e:Expr) = Sub(this, e)
   def *(e:Expr) = Mul(this, e)
   def /(e:Expr) = Div(this, e)
}

case class ValInt(value:Int)        extends Expr
case class ValLong(value:Long)      extends Expr
case class ValFloat(value:Float)    extends Expr
case class ValDouble(value:Double)  extends Expr

case class Var(id:Int)              extends Expr

case class Add(left:Expr, right:Expr) extends Expr
case class Sub(left:Expr, right:Expr) extends Expr
case class Mul(left:Expr, right:Expr) extends Expr
case class Div(left:Expr, right:Expr) extends Expr

case class Tuple(elems:Vector[Expr]) extends Expr
case class TupleExtractor(tuple:Tuple, index:Int) extends Expr

case class TypedExpr(e:Expr, typ:Type) extends Expr
