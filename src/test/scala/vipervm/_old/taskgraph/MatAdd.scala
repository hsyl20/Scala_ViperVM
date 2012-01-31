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

package org.vipervm.tests

import org.vipervm.platform.{ReadOnly,ReadWrite}
import org.vipervm.taskgraph._

class MatAdd(a:Data,b:Data,c:Data) extends Task {
  val name = "matadd"
  val args = Seq(a,b,c)
  val argModes = Seq(ReadOnly,ReadOnly,ReadWrite)

  val source = "//TODO"
}
