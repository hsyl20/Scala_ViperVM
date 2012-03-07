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
import org.vipervm.runtime.mm._
import org.vipervm.runtime.interpreter._

abstract class MatrixDSL extends Program {
  val term:Term
  val symbols:SymbolTable
  val peer:Option[Matrix]

  def +(m:MatrixDSL):MatrixDSL = {
    val myt = term
    val myc = symbols
    new MatrixDSL {
      val term = TmApp(TmId("+"), Seq(myt, m.term))
      val symbols = SymbolTable(myc.values ++ m.symbols.values)
      val peer = None
    }
  }

  def *(m:MatrixDSL):MatrixDSL = {
    val myt = term
    val myc = symbols
    new MatrixDSL {
      val term = TmApp(TmId("*"), Seq(myt, m.term))
      val symbols = SymbolTable(myc.values ++ m.symbols.values)
      val peer = None
    }
  }
}
