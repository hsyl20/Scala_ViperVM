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

package org.vipervm.tests.runtime

import org.vipervm.tests.platform.MatAddKernel
import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.runtime.data.Matrix2D

class TMatAddKernel extends TaskKernel {
  val peer = new MatAddKernel
  
  def makeKernelParams(params:Seq[Value],memory:MemoryNode):Seq[KernelParameter] = {
    params match {
      case Seq(DataValue(a:Matrix2D), DataValue(b:Matrix2D), DataValue(c:Matrix2D)) => {
        val w = a.width.toInt
        val h = a.height.toInt
        val b1 = a.viewIn(memory).get.buffer
        val b2 = b.viewIn(memory).get.buffer
        val b3 = c.viewIn(memory).get.buffer
        Seq(IntKernelParameter(w),IntKernelParameter(h),BufferKernelParameter(b1),BufferKernelParameter(b2),BufferKernelParameter(b3))
        //TODO: handle padding and offset
      }
      case _ => throw new Exception("invalid parameters: "+params)
    }
  }
}

class FMatAddKernel extends FunctionalKernel {
  val peer = new TMatAddKernel

  def createTask(args:Seq[FutureValue]):FutureEvent[Task] = args.map(_.value) match {
    case Seq(aa@DataValue(a:Matrix2D), bb@DataValue(b:Matrix2D)) => {
      val c = DataValue(new Matrix2D(a.elemSize, a.width, a.height))
      val task = Task(peer, List(aa,bb,c), c)
      FutureEvent(task)
    }
    case _ => throw new Exception("Invalid parameters: "+args)
  }
}
