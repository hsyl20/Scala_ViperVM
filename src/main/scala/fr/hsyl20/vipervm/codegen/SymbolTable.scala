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

package fr.hsyl20.vipervm.codegen

import scala.collection.immutable.HashMap

abstract class SymbolTable[A] {
   val items:HashMap[String,A]

   def + (item: (String,A)) = new SymbolTable[A] {
      val items = SymbolTable.this.items + item
   }

   def get(s:String): Option[A] = items.get(s)

   def declare(item: (Symbol,A)) = this + (item._1.name, item._2)

   /* Perform fusion of two symbol tables */
   def |+| (ss:SymbolTable[A]): SymbolTable[A] = {
      (this /: ss.items) { (st, entry) =>
         st.get(entry._1) match {
            case None => this + entry
            case Some(desc2) => system.error("Conflicting symbols")
         }
         st
      }
   }

}

object SymbolTable {
   def empty[A]: SymbolTable[A] = new SymbolTable[A] {
      val items = HashMap.empty[String,A]
   }
}
