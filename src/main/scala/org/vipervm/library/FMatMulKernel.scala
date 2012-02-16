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

package org.vipervm.library

import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.runtime.data.Matrix2D
import org.vipervm.runtime.interpreter.{Value,DataValue,FutureValue}

class TMatMulKernel extends TaskKernel {
  val peer = new MatMulKernel
  
  def makeKernelParams(params:Seq[Value],memory:MemoryNode):Seq[KernelParameter] = {
    params match {
      case Seq(DataValue(a:Matrix2D[_]), DataValue(b:Matrix2D[_]), DataValue(c:Matrix2D[_])) => {
	val n = a.width.toInt
	val b1 = a.viewIn(memory).get.buffer
	val b2 = b.viewIn(memory).get.buffer
	val b3 = c.viewIn(memory).get.buffer
	Seq(IntKernelParameter(n),BufferKernelParameter(b1),BufferKernelParameter(b2),BufferKernelParameter(b3))
	//TODO: handle padding and offset
      }
      case _ => throw new Exception("invalid parameters: "+params)
    }
  }
}


class FMatMulKernel extends FunctionalKernel {
  val peer = new TMatMulKernel

  def createTask(args:Seq[FutureValue]):FutureEvent[Task] = args.map(_.value) match {
    case Seq(aa@DataValue(a:Matrix2D[_]), bb@DataValue(b:Matrix2D[_])) => {
      val c = DataValue(new Matrix2D[Float](b.width, a.height))
      val task = Task(peer, List(aa,bb,c), c)
      FutureEvent(task)
    }
    case _ => throw new Exception("Invalid parameters: "+args)
  }
}