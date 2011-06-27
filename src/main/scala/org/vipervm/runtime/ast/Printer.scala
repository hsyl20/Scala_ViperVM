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

package org.vipervm.runtime.ast

object Printer {
  def print(ctx:Context, t:Term, out:java.io.PrintStream = System.out): Unit = t match {
    case AbsTerm(body,name) => {
      val (ctx2, name2) = ctx.pickFreshName(name)
      out.print("(λ" + name2 + ". ")
      print(ctx2,body)
      out.print(")")
    }
    case AppTerm(left,right) => {
      out.print("(")
      print(ctx, left)
      out.print(" ")
      print(ctx, right)
      out.print(")")
    }
    case VarTerm(idx,n) => {
      out.print(
        if (ctx.length == n) ctx.indexToName(idx) else "[bad index]"
      )
    }
  }
}
