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

package org.vipervm.runtime.mm.config

import org.vipervm.platform._
import org.vipervm.runtime.mm._

case class DataConfig(constraints:DataConfigConstraint*)

sealed abstract class DataConfigConstraint

case class PresentIn(data:Data,memory:MemoryNode) extends DataConfigConstraint {
  def withRepr(repr:Repr) = PresentInWithRepr(data,memory,repr)
}

case class PresentInWithRepr(data:Data,memory:MemoryNode,repr:Repr) extends DataConfigConstraint
