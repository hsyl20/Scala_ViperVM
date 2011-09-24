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

import org.vipervm.platform._

/**
 * JVM platform driver
 *
 */
class JVMDriver extends Driver {

  val processors:Seq[JVMProcessor] = Seq(new JVMProcessor)

  val networks:Seq[JVMNetwork] = Seq(new JVMNetwork)

  val memories:Seq[JVMMemoryNode.type] = Seq(JVMMemoryNode)

}
