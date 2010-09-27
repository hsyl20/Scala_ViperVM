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

import fr.hsyl20.auratune._
import scala.collection.immutable._

object Symbol {
   private var id = 0
   def newId: String = { id += 1 ; "v"+id }
}

class Matrix[A <: AnyVal](val width:Int, val height:Int, val depth:Int)(implicit m: CLMatrix[A]) {

   def this(width:Int, height:Int)(implicit m: CLMatrix[A]) = this(width, height, 1)(m)
   def this(width:Int)(implicit m: CLMatrix[A]) = this(width, 1, 1)(m)
   def this()(implicit m: CLMatrix[A]) = this(1, 1, 1)(m)

   def map(ef:ExprFun): Codelet = {
      val arg = new Argument(m.ctype, Size(m.ctype.size * width * height * depth))
      val c = new CodeletBuilder(arg)
      val cell = Var(arg.id+"[get_global_id(0)]")
      val stat = Assign(cell, ef(cell))
      c.addStatement(stat)
      c.codelet()
   }
}

abstract class CLMatrix[A] {
   def ctype: CType
}

object CLMatrix {
   implicit object FloatMatrix extends CLMatrix[Float] {
      def ctype = Float*
   }

   implicit object DoubleMatrix extends CLMatrix[Double] {
      def ctype = Double*
   }
}

