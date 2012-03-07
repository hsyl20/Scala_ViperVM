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

package org.vipervm.runtime

import org.vipervm.platform.AccessMode
import org.vipervm.runtime.mm.{Repr,VVMType}

object Prototype {
  def apply(ps:Parameter[_]*):List[Parameter[_]] = ps.toList
}

case class Parameter[A](mode:AccessMode, repr:Repr, typ:VVMType, storage:Storage = DeviceStorage, name:String = "", description:String = "")

sealed abstract class Storage
case object HostStorage extends Storage
case object DeviceStorage extends Storage

