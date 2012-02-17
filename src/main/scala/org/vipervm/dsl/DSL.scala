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

package org.vipervm.dsl

import org.vipervm.utils._
import org.vipervm.runtime.data.Primitives._
import org.vipervm.runtime.data
import org.vipervm.runtime.interpreter._

import org.vipervm.library._

abstract class Matrix2D[A:Primitive] extends Program {
  val term:Term
  val symbols:SymbolTable
  val peer:Option[data.Matrix2D[A]]

  lazy val addFunc = new MatAddFunction
  lazy val mulFunc = new MatMulFunction

  def +(m:Matrix2D[A]):Matrix2D[A] = {
    val myt = term
    val myc = symbols
    new Matrix2D[A] {
      val term = TmApp(TmKernel("matadd"), Vector(myt, m.term))
      val symbols = SymbolTable(myc.values ++ m.symbols.values, myc.functions ++ m.symbols.functions + ("matadd" -> addFunc))
      val peer = None
    }
  }

  def *(m:Matrix2D[A]):Matrix2D[A] = {
    val myt = term
    val myc = symbols
    new Matrix2D[A] {
      val term = TmApp(TmKernel("matmul"), Vector(myt, m.term))
      val symbols = SymbolTable(myc.values ++ m.symbols.values, myc.functions ++ m.symbols.functions + ("matmul" -> mulFunc))
      val peer = None
    }
  }
}

object Matrix2D {
  var id = -1

  def apply[A:Primitive](width:Long,height:Long):Matrix2D[A] = {
    id += 1
    val name = "m%d".format(id)
    new Matrix2D[A] {
      val term = TmVar(name)
      val peer = Some(new data.Matrix2D[A](width,height))
      val symbols = SymbolTable(Map(name -> peer.get), Map.empty)
    }
  }
}
