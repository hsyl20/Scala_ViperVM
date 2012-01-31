package org.vipervm.dsl.linearalgebra

class WrappedTuple2[A,B](val expr:Expr[(A,B)]) {
  def _1 = MethodCall[A](expr, "_1")
  def _2 = MethodCall[A](expr, "_2")

  def toTuple = (_1,_2)
}

object WrappedTuple2 {
  def unapply[A,B](e:WrappedTuple2[A,B]): Option[(Expr[A],Expr[B])] = Some((MethodCall[A](e.expr,"_1"), MethodCall[B](e.expr,"_2")))
}
