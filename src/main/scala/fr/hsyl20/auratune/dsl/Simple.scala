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

package fr.hsyl20.auratune.dsl

import fr.hsyl20.auratune.Graph

class SimpleDSL(outputs:List[Data], bindings:collection.Map[Data,Symbol]) {

   val inputs = outputs.map(x => Graph.leaves(x)).flatten[Data].distinct

   //TODO: check that binding exists for inputs and outputs
   val inouts = (
      inputs.map(x => "starpu_data * %s".format(bindings(x).name)) :::
      outputs.map (x => "starpu_data ** %s".format(bindings(x).name)))

   val fun = "void fun(%s) {\n".format(inouts mkString(", "))

   println(fun)
}

object SimpleDSL {
   def compute(outputs:Data*) = new {
      def where(bindings:(Data,Symbol)*) = new SimpleDSL(outputs.toList, bindings.toMap)
   }
}
