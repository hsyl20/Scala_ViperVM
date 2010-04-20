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
**                     GPLv3
*/

package fr.hsyl20.auratune.codelet

import fr.hsyl20.auratune.Codelet

sealed abstract class Area
case object Global extends Area
case object Local extends Area
case object Constant extends Area

sealed abstract class CType {
   def * : CType = Pointer(this)
}
case object Float extends CType
case object Double extends CType
case object Int extends CType
case class Pointer(t:CType) extends CType

case class Argument(ctype:CType, area:Area = Global) {
   val id: String = Symbol.newId
}

class CodeletBuilder(arguments:Argument*) {

   private var statements: List[Assign] = Nil

   def addStatement(s:Assign): CodeletBuilder = {
      statements = s :: statements
      this
   }

   def codelet(name:String = Symbol.newId): Codelet = {
      val src = "__kernel void " + name + 
         "(" + arguments.map(clArg).mkString(", ") + ") { \n" + 
            statements.map(clStatement).mkString("\n") +
         "\n}"
      new Codelet(name, src)
   }
   private def clArg(a:Argument): String = a.ctype match {
      case Pointer(_) => clArea(a.area) + " " + clType(a.ctype) + " " + a.id
      case _ => clType(a.ctype) + " " + a.id
   }

   def clArea(a:Area): String = a match {
      case Global => "__global"
      case Local => "__local"
      case Constant => "__constant"
   }

   def clType(ct:CType): String = ct match {
         case Float => "float"
         case Double => "double"
         case Int => "int"
         case Pointer(t) => clType(t) + "*"
   }

   private def clStatement(s:Assign): String = {
      val Assign(v,e) = s
      v.s + "=" + e.toCL + ";"
   }
}
