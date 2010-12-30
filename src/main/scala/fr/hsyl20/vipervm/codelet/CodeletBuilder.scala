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

package fr.hsyl20.vipervm.codelet
/*

import fr.hsyl20.vipervm.Codelet
import fr.hsyl20.vipervm.{ArgSize, Arg}
import fr.hsyl20.vipervm.{Mode,ReadWrite,ReadOnly,WriteOnly}


sealed abstract class CType {
   def * : CType = Pointer(this)
   def size: Int
}
case object Float extends CType { def size = 4 }
case object Double extends CType { def size = 8 }
case object Int extends CType {def size = 4 }
case class Pointer(t:CType) extends CType { def size = 8 }

case class Argument(ctype:CType, size:ArgSize, mode:Mode = ReadWrite, area:Area = Global) {
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
      new Codelet(name, src, arguments.map(a => scala.Symbol(a.id) -> new Arg(a.size, a.mode)).toMap)
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
*/
