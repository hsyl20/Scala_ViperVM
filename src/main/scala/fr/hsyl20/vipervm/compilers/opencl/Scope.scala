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

package fr.hsyl20.vipervm.codegen.opencl

import scala.collection.immutable.Stack
import scala.collection._

trait Scope extends Block {
   private var symbols: Stack[mutable.Map[String,Any]] = new Stack

   symbols = symbols.push(new mutable.HashMap)

   def addSymbol(s:String, a:Any): Unit = {
      val m = symbols.head
      if (m.isDefinedAt(s)) 
         throw new Exception("Symbol \"%s\" already declared".format(s))
      m += (s -> a)
   }

   def getSymbol(s:String, ss:Stack[mutable.Map[String,Any]] = symbols): Option[Any] = {
      ss.headOption flatMap (_.get(s) orElse getSymbol(s, ss.tail))
   }

   def checkSymbol(s:String): Option[Any] = {
      val ret = getSymbol(s)
      
      if (ret == None)
         throw new Exception("Symbol \"%s\" not declared in:\n%s".format(s, code))
      
      ret
   }

   override def scopeIn: Unit  = {
      symbols = symbols.push(new mutable.HashMap)
      super.scopeIn
   }

   override def scopeOut: Unit = {
      super.scopeOut
      symbols = symbols.pop
   }
}
