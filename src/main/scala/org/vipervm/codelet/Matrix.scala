/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

/*
package org.vipervm.codelet

import org.vipervm._
import scala.collection.immutable._

class Matrix[A <: AnyVal](val width:Int, val height:Int, val depth:Int)(implicit m: CLMatrix[A]) extends Data {

   def this(width:Int, height:Int)(implicit m: CLMatrix[A]) = this(width, height, 1)(m)
   def this(width:Int)(implicit m: CLMatrix[A]) = this(width, 1, 1)(m)
   def this()(implicit m: CLMatrix[A]) = this(1, 1, 1)(m)

   type BufferType = MatrixBuffer

   val size = width * height * depth * m.elementSize

   def createBuffer(device:Device): MatrixBuffer = {
      val b = new MatrixBuffer(device, size, 0)
      buffers += (device -> b)
      b
   }

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
   val ctype: CType
   val elementSize: Int
}

object CLMatrix {
   implicit object FloatMatrix extends CLMatrix[Float] {
      val ctype = Float*
      val elementSize = 4
   }

   implicit object DoubleMatrix extends CLMatrix[Double] {
      val ctype = Double*
      val elementSize = 8
   }
}

*/
