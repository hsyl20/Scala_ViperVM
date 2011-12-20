package org.vipervm.functional

sealed abstract class Expr {
  def apply(x:Expr):Application = Application(this,x)
}
case class Abstraction(f:Expr=>Expr) extends Expr {
  override def equals(abs:Any) = abs match {
    case a:Abstraction => f(DummyExpr) == a.f(DummyExpr)
    case _ => false
  }
}
case class Application(exprs:Expr*) extends Expr {
  override def apply(x:Expr):Application = Application(exprs :+ x :_*)
}

case object DummyExpr extends Expr
case class Var(name:String) extends Expr

sealed abstract class Op extends Expr
case object Reduce extends Op
case object FMap extends Op
case object ZipWith extends Op
case object CrossWith extends Op
case object Transpose extends Op
case object Split extends Op
case object Join extends Op
case object Plus extends Op
case object Minus extends Op
case object Time extends Op

case class Data(id:Int) extends Expr
