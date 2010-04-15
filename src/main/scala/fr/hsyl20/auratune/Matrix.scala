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


object Symbol {
   private var id = 0
   def newId: String = { id += 1 ; "v"+id }
}

abstract class Argument {
   val id: String = Symbol.newId
   val cType: String
}

class FloatMatrix {
   val cType = "float *"
   val id = Symbol.newId
   
   def cell: Var = Var(id+"[get_global_id(0)]")

   def forAll(ef:ExprFun, name:String): String = "__kernel void " + name + 
      "( __global float * " + id + ") { \n" + 
      Assign(cell, ef(cell)).toCL +
      "\n}"
}

class Codelet(name:String, args:Argument*) {
   
   private var statements: List[(Assign, Map[String, Argument])] = Nil

   def addStatement(s:Assign, map:Map[String,Argument]): Unit = {
      statements = (s,map) :: statements
   }

   def cl: String = "__kernel void " + name + 
      "(" + args.map(a => "__global " + a.cType + a.id).mkString(", ") + ") { \n" + 
         statements.map((a) => compile(a._1,a._2)).mkString("\n") +
      "\n}"

   private def compile(e:Expr, m:Map[String,Argument]): (String, String) = e match {
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

   private def compile(s:Assign, m:Map[String,Argument]): String = {
      val Assign(v,e) = s
      val (c,sym) = compile(e, m)
      val vid = m.get(v.s) match {
         case Some(arg) => arg.id
         case None => throw new Exception("Symbol doesn't match any argument")
      }
      c + vid + "=" + sym + ";"
   }
}
