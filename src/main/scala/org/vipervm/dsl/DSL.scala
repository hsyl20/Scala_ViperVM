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
import org.vipervm.runtime.interpreter._

class MatrixDSL(val term:Term) {

  def +(m:MatrixDSL):MatrixDSL = new MatrixDSL(TmApp(TmId("+"), Seq(term,m.term)))

  def *(m:MatrixDSL):MatrixDSL = new MatrixDSL(TmApp(TmId("*"), Seq(term,m.term)))
}
