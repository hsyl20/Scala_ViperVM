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

package org.vipervm

import org.vipervm.runtime.interpreter._
import org.vipervm.runtime.mm.Matrix

package object dsl {

  var id = -1

  implicit def matrixWrapper(m:Matrix) = {
    id += 1
    val name = "m%d".format(id)
    new MatrixDSL {
      val term = TmId(name)
      val peer = Some(m)
      val symbols = SymbolTable(Map(name -> m))
    }
  }

  def let(bindings:(Program,Program)*) = new {
    val bs = bindings.map{ case (x,e) => x.term match {
      case a@TmId(_) => (a,e)
      case _ => throw new Exception("Invalid data type")
    }}

    def in(prog:Program):Program = new Program {
      
      val term = (bs :\ prog.term) { case ((v,e),in) => TmLet(v,e.term,in) }
      val symbols = (prog.symbols /: bindings.map(_._2.symbols)) {
        (a,b) => a.copy(a.values ++ b.values)
      }
    }
  }
}

