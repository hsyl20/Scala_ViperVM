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

package org.vipervm.library

import org.vipervm.runtime._
import org.vipervm.library.linearalgebra._

object DefaultLibrary {
  def apply():Library = new Library(
    Function(FloatMatrixAdditionMetaKernel, MatrixAdditionProto),
    Function(FloatMatrixMultiplicationMetaKernel, MatrixMultiplicationProto)
  )
}
