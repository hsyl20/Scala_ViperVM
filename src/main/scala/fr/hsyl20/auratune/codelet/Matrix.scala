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

package fr.hsyl20.auratune.codelet

import fr.hsyl20.auratune._
import scala.collection.immutable._


object Symbol {
   private var id = 0
   def newId: String = { id += 1 ; "v"+id }
}


class FloatMatrix {
   def map(ef:ExprFun): Codelet = {
      val arg = new Argument(Float*)
      val c = new CodeletBuilder(arg)
      val cell = Var(arg.id+"[get_global_id(0)]")
      val stat = Assign(cell, ef(cell))
      c.addStatement(stat)
      c.codelet()
   }
}

