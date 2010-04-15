package fr.hsyl20.auratune

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

class Codelet(arguments:Argument*) {

   private var statements: List[Assign] = Nil

   def addStatement(s:Assign): Codelet = {
      statements = s :: statements
      this
   }

   def cl(name:String): String = "__kernel void " + name + 
      "(" + arguments.map(clArg).mkString(", ") + ") { \n" + 
         statements.map(clStatement).mkString("\n") +
      "\n}"

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
