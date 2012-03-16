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

import akka.dispatch.{Future,Await,ExecutionContext}
import org.vipervm.platform._

class JVMProcessor(hostDriver:HostDriver,dispatcher:ExecutionContext) extends Processor {
  type MemoryNodeType = HostMemoryNode

  val memories = hostDriver.memories

  def compile(kernel:Kernel):Unit = {}

  def execute(kernel:Kernel, args:Seq[Any]): KernelExecution = {
    implicit def ker2ker(k:Kernel):JVMKernel =
      k.asInstanceOf[JVMKernel]

    val ev = new UserEvent

    Future {
      kernel.fun(args)
      ev.complete
    }(dispatcher)

    new KernelExecution(kernel, args, this, ev)
  }

  override def toString = "JVM: Akka Actors"
}
