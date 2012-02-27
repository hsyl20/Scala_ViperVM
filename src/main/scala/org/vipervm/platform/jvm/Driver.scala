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
import akka.actor.ActorSystem

/**
 * JVM platform driver
 *
 */
class JVMDriver(hostDriver:HostDriver) extends Driver {

  val procCount = Runtime.getRuntime.availableProcessors
  val dispatcher = ActorSystem().dispatcher

  val processors = (0 to procCount).map(x => new JVMProcessor(hostDriver,dispatcher))

  val networks:Seq[Network] = Seq.empty

  val memories:Seq[MemoryNode] = Seq.empty

}
