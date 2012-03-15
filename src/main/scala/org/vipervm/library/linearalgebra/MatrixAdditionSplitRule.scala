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

  def rewrite(term:Term):Option[Term] = term match {
    case TmApp(TmId("+"), Seq(TmData(d1), TmData(d2))) => {
      (d1.typ,d2.typ,d1.meta,d2.meta) match {
        case (Some(MatrixType(elemTyp1)),
              Some(MatrixType(elemTyp2)),
              Some(MatrixMetaData(w1,h1)),
              Some(MatrixMetaData(w2,h2))) if elemTyp1 == elemTyp2 => {
                if (w1*h1*elemTyp1.size > 100*1024*1024) {
                  Some(splitted(TmData(d1),TmData(d2),TmId("s")))
                }
                else None
              }
        case _ => None
      }
    }
    case _ => None
  }

}
