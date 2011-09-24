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

package org.vipervm.platform.jvm

import org.vipervm.platform.{HostMemoryNode,Link}

case class JVMReadLink( network:JVMNetwork, target:HostMemoryNode) extends Link {
  val source = JVMMemoryNode
}

case class JVMWriteLink( network:JVMNetwork, source:HostMemoryNode) extends Link {
  val target = JVMMemoryNode
}
