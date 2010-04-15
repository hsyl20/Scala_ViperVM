/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**
*/

package fr.hsyl20.auratune

import scala.collection.immutable._


sealed abstract class Expr {
   def +(e:Expr) = Add(this, e)
}

case class Var(s:Symbol) extends Expr {
   def :=(e:Expr) = Assign(this, e)
}

case class Add(e1:Expr, e2:Expr) extends Expr
case class Assign(v:Var, e:Expr)


package object Conversions {
   var symbolMap: Map[Symbol, Var] = Map.empty

   implicit def sym2var(s:Symbol): Var = symbolMap.get(s) match {
      case Some(v) => v
      case None => {
         val v = Var(s)
         symbolMap = symbolMap + (s -> v)
         v
      }
   }
}

object Symbol {
   private var id = 0
   def newId: String = { id += 1 ; "v"+id }
}

abstract class Argument {
   val id: String = Symbol.newId
   val cType: String
}

class FloatMatrix extends Argument {
   val cType = "float *"
}

class Codelet(name:String, args:Argument*) {
   
   private var statements: List[(Assign, Map[Symbol, Argument])] = Nil

   def addStatement(s:Assign, map:Map[Symbol,Argument]): Unit = {
      statements = (s,map) :: statements
   }

   def cl: String = "__kernel void " + name + 
      "(" + args.map(a => "__global " + a.cType + a.id).mkString(", ") + ") { " + 
         statements.map((a) => compile(a._1,a._2)).mkString("\n") +
      "}"

   private def compile(e:Expr, m:Map[Symbol,Argument]): (String, String) = e match {
      case Var(s) => m.get(s) match {
         case Some(arg) => ("", arg.id)
         case None => throw new Exception("Symbol doesn't match any argument")
      }
      case Add(e1,e2) => {
         val (s1, a) = compile(e1,m)
         val (s2, b) = compile(e2,m)
         val s = Symbol.newId
         (s1 + s2 + s + " = " + a +"+"+b+";", s)
      }
   }

   private def compile(s:Assign, m:Map[Symbol,Argument]): String = {
      val Assign(v,e) = s
      val (c,sym) = compile(e, m)
      val vid = m.get(v.s) match {
         case Some(arg) => arg.id
         case None => throw new Exception("Symbol doesn't match any argument")
      }
      c + vid + "=" + sym + ";"
   }
}
