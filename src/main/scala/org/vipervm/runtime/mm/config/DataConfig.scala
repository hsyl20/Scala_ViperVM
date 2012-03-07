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

sealed abstract class DataConfig {
  def || (that:DataConfig) = Or(this,that)
  def && (that:DataConfig) = And(this,that)
}

case class Or(left:DataConfig,right:DataConfig) extends DataConfig
case class And(left:DataConfig,right:DataConfig) extends DataConfig

case class RequiredMetaData(data:Data) extends DataConfig
case class RequireInHostMemory(data:Data) extends DataConfig

case class RequiredIn(data:Data,memory:MemoryNode) extends DataConfig {
  def withRepr(repr:Repr) = RequiredInWithRepr(data,memory,repr)
}

case class RequiredInWithRepr(data:Data,memory:MemoryNode,repr:Repr) extends DataConfig
