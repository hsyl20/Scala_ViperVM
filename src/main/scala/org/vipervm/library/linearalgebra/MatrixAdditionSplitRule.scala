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

package org.vipervm.library.linearalgebra

import org.vipervm.runtime.Rule
import org.vipervm.runtime.mm._
import org.vipervm.runtime.interpreter._

object MatrixAdditionSplitRule extends Rule {
  val prototype = MatrixAdditionProto

  private def splitted(a:Term,b:Term,splitter:Term):Term = {
    TmApp(TmId("flatten"),
      Seq(TmApp(TmId("zipWith"),
        Seq(TmId("+"),
          TmApp(TmId("split"),
            Seq(splitter, a)),
          TmApp(TmId("split"),
            Seq(splitter, b))))))
  }

  def rewrite(term:Term,typ:VVMType,meta:MetaData):Option[Term] = term match {
    case TmApp(TmId("+"), Seq(TmData(d1), TmData(d2))) => {
      val MatrixMetaData(w,h) = meta
      val MatrixType(elemTyp) = typ

      if (w*h*elemTyp.size > 100*1024*1024) {
        Some(splitted(TmData(d1),TmData(d2),TmId("s")))
      }
      else None
    }
    case _ => None
  }

}
