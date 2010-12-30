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

package fr.hsyl20.vipervm.dsl

import fr.hsyl20.vipervm.Graph

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
