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

object Printer {

  def print[A](e:Expr[A]): String = e match {
    case FunCall(name, as@_*) => "%s(%s)".format(name, as.mkString(","))
    case ClosureCall(f,a) => "%s(%s)".format(print(f),print(a))
    case MethodCall(self, name, as@_*) => {
      val as2 = for (a <- as) yield print(a)
      "%s.%s(%s)".format(print(self),name,as2.mkString(","))
    }
    case Value(v) => v.toString
    case ExprFun1(f) => {
      print(Dummy(0)) + " => " + print(f(Dummy(0)))
    }
    case Choice(cs@_*) => cs.map(print _).mkString(" | ")
    case Dummy(n) => "d"+n
  }
}
