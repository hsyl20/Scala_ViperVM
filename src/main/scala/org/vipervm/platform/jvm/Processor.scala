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

import scala.actors.Futures._
import org.vipervm.platform._

class JVMProcessor(hostDriver:HostDriver) extends Processor {
  type MemoryNodeType = HostMemoryNode

  val memories = hostDriver.memories

  def compile(kernel:Kernel):Unit = {}

  def execute(kernel:Kernel, args:Seq[Any]): KernelEvent = {
    implicit def ker2ker(k:Kernel):JVMKernel =
      k.asInstanceOf[JVMKernel]

    val ev = new UserEvent

    future { 
      kernel.fun(args)
      ev.complete
    }

    new KernelEvent(kernel, args, this, ev)
  }

  override def toString = "JVM: Scala Actors"
}
