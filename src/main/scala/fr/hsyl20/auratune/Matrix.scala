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


class FloatMatrix {
   def forAll(ef:ExprFun): Codelet = {
      val c = new Codelet
      val arg = c.addArgument(Float*)
      val cell = Var(arg.id+"[get_global_id(0)]")
      val stat = Assign(cell, ef(cell))
      c.addStatement(stat)
   }
}

