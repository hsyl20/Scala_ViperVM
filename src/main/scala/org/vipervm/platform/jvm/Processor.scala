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

class JVMProcessor extends Processor {
  type MemoryNodeType = JVMMemoryNode.type

  val memories = Seq(JVMMemoryNode)

  def compile(kernel:Kernel):Unit = {}

  def execute(kernel:Kernel, args:Seq[Any]): KernelEvent = {
    implicit def ker2ker(k:Kernel):JVMKernel =
      k.asInstanceOf[JVMKernel]

    implicit def buf2buf(b:Buffer): JVMBuffer =
      b.asInstanceOf[JVMBuffer]

    val ev = new JVMEvent(future { 
      kernel.fun(args.map { 
        case JVMBufferKernelParameter(b) => b.asInstanceOf[JVMBuffer]
        case JVMIntKernelParameter(v) => v
        case JVMLongKernelParameter(v) => v
        case JVMDoubleKernelParameter(v) => v 
        case JVMFloatKernelParameter(v) => v
      })
    })

    new KernelEvent(kernel, args, this, ev)
  }

  override def toString = "JVM"
}
