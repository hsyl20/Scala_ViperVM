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

import org.vipervm.tests.lowlevel.MatMulKernel
import org.vipervm.platform.{MemoryNode,KernelParameter,IntKernelParameter,BufferKernelParameter}
import org.vipervm.runtime._
import org.vipervm.runtime.data.Matrix2D

class TMatMulKernel extends TaskKernel {
  val peer = new MatMulKernel
  
  def makeKernelParams(params:Seq[TaskParameter],memory:MemoryNode):Seq[KernelParameter] = {
    params match {
      case DataTaskParameter(a:Matrix2D) :: DataTaskParameter(b:Matrix2D) :: DataTaskParameter(c:Matrix2D) :: Nil => {
	val n = (a.width * a.height).toInt
	val b1 = a.viewIn(memory).get.buffer
	val b2 = b.viewIn(memory).get.buffer
	val b3 = c.viewIn(memory).get.buffer
	Seq(IntKernelParameter(n),BufferKernelParameter(b1),BufferKernelParameter(b2),BufferKernelParameter(b3))
	//TODO: handle padding and offset
      }
      case _ => throw new Exception("invalid parameters")
    }
  }
}


class FMatMulKernel extends FunctionalKernel {
  val peer = new TMatMulKernel

  def createTask(args:Seq[TaskParameter]):(Task,Data) = args match {
    case (a:Matrix2D) :: (b:Matrix2D) :: Nil => {
      val c = new Matrix2D(a.elemSize, b.width, a.height)
      val task = Task(peer, List(a,b,DataTaskParameter(c)))
      (task,c)
    }
    case _ => throw new Exception("Invalid parameters")
  }
}
