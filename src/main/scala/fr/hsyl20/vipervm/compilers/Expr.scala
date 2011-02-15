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

package fr.hsyl20.vipervm.compilers

sealed abstract class Expr {
  def +(e:Expr) = Add(this,e)
  def -(e:Expr) = Sub(this,e)
  def *(e:Expr) = Mul(this,e)
  def /(e:Expr) = Div(this,e)
  def ===(e:Expr) = E(this,e)
  def !=(e:Expr) = NE(this,e)
  def >(e:Expr) = G(this,e)
  def >=(e:Expr) = GE(this,e)
  def <(e:Expr) = B(this,e)
  def <=(e:Expr) = BE(this,e)
}

case class Add(e1:Expr,e2:Expr) extends Expr
case class Sub(e1:Expr,e2:Expr) extends Expr
case class Mul(e1:Expr,e2:Expr) extends Expr
case class Div(e1:Expr,e2:Expr) extends Expr
case class E(e1:Expr,e2:Expr) extends Expr
case class NE(e1:Expr,e2:Expr) extends Expr
case class GE(e1:Expr,e2:Expr) extends Expr
case class G(e1:Expr,e2:Expr) extends Expr
case class B(e1:Expr,e2:Expr) extends Expr
case class BE(e1:Expr,e2:Expr) extends Expr

case object Dummy extends Expr
