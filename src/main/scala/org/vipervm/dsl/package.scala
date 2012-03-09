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

  implicit def matrixWrapper(m:Matrix) = new MatrixDSL(TmData(m.data))

  implicit def dsl2term(m:MatrixDSL):Term = m.term
}
