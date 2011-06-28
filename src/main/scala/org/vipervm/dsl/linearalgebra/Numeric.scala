package org.vipervm.dsl.linearalgebra

class Num[A]

class WrappedNum[A](expr:Expr[Num[A]]) {
  def sqrt = MethodCall[Num[A]](expr, "sqrt")
  def -(a:Expr[Num[A]]) = MethodCall[Num[A]](expr, "-", a)
  def +(a:Expr[Num[A]]) = MethodCall[Num[A]](expr, "+", a)
  def *(a:Expr[Num[A]]) = MethodCall[Num[A]](expr, "*", a)
  def /(a:Expr[Num[A]]) = MethodCall[Num[A]](expr, "/", a)
}

class WrappedMN[A](expr:Expr[Matrix[Num[A]]]) {
  def -(a:Expr[Matrix[Num[A]]]) = (expr zip a) map (x => x._1 - x._2)
  def +(a:Expr[Matrix[Num[A]]]) = (expr zip a) map (x => x._1 + x._2)
  def *(a:Expr[Matrix[Num[A]]]) = MethodCall[Matrix[Num[A]]](expr, "*", a)
}
