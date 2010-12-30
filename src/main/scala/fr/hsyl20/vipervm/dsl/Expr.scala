/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.dsl

class Expr extends Data {
   def + (e:Expr) = Plus(this,e)
   def - (e:Expr) = Sub(this,e)
   def * (e:Expr) = Mul(this,e)
   def / (e:Expr) = Div(this,e)
}


case class Plus(e1:Expr, e2:Expr) extends Expr
case class Sub(e1:Expr, e2:Expr) extends Expr
case class Mul(e1:Expr, e2:Expr) extends Expr
case class Div(e1:Expr, e2:Expr) extends Expr

case object DummyExpr extends Expr
