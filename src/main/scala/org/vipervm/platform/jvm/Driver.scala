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
class JVMDriver(hostDriver:HostDriver) extends Driver {

  val processors = Seq(new JVMProcessor(hostDriver))

  val networks:Seq[Network] = Seq.empty

  val memories:Seq[MemoryNode] = Seq.empty

}
