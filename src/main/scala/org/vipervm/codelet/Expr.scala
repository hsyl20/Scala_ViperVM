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

/*
package org.vipervm.codelet


sealed abstract class Expr {
   def +(e:Expr) = Add(this, e)
   def -(e:Expr) = Sub(this, e)

   def toCL: String
   def replace(s:Expr,d:Expr): Expr
   override def toString = toCL
}

case class ValInt(i:Int) extends Expr {
   def toCL = i.toString
   def replace(s:Expr, d:Expr): Expr = this
}

case class ValFloat(f:Float) extends Expr {
   def toCL = f.toString
   def replace(s:Expr, d:Expr): Expr = this
}

case class Var(s:String = Symbol.newId) extends Expr {
   def :=(e:Expr) = Assign(this, e)

   def -->(e:Expr) = ExprFun(this, e)

   def toCL = s
   def replace(s:Expr, d:Expr): Expr = this
}

case class Add(e1:Expr, e2:Expr) extends Expr {
   def replace(s:Expr, d:Expr): Expr = {
      val e1b = if (e1 == s) d else e1
      val e2b = if (e2 == s) d else e2
      Add(e1b,e2b)
   }

   def toCL: String = "(" + e1.toCL + " + " + e2.toCL + ")"

}

case class Sub(e1:Expr, e2:Expr) extends Expr {
   def replace(s:Expr, d:Expr): Expr = {
      val e1b = if (e1 == s) d else e1
      val e2b = if (e2 == s) d else e2
      Sub(e1b,e2b)
   }

   def toCL: String = "(" + e1.toCL + " - " + e2.toCL + ")"

}

case class Assign(v:Var, e:Expr) {
   def toCL: String = v.s + " = "+ e.toCL + ";"
}

case class ExprFun(v:Var, e:Expr) {
   def apply(ex:Expr): Expr = e.replace(v, ex)
   def +(ex:Expr): ExprFun = ExprFun(v, e+ex)
}


object Conversions {
   var symbolMap: Map[Symbol, Var] = Map.empty

   implicit def sym2var(s:Symbol): Var = symbolMap.get(s) match {
      case Some(v) => v
      case None => {
         val v = Var(s.toString)
         symbolMap = symbolMap + (s -> v)
         v
      }
   }

   implicit def int2val(i:Int): ValInt = ValInt(i)
   implicit def float2val(f:Float): ValFloat = ValFloat(f)
}
*/
