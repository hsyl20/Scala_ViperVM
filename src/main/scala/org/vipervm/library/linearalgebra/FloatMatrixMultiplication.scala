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

package org.vipervm.library.linearalgebra

import org.vipervm.library.linearalgebra.kernels.jvm.FloatMatrixMultiplicationJVM
import org.vipervm.library.linearalgebra.kernels.opencl.FloatMatrixMultiplicationOpenCL
import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.runtime.data.Matrix2D
import org.vipervm.library.linearalgebra.kernels._

object FloatMatrixMultiplicationMetaKernel extends KernelSet {
  val kernels = Seq(FloatMatrixMultiplicationOpenCL,FloatMatrixMultiplicationJVM)
  
  val a = Parameter[Matrix2D[Float]](
    name = "a",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val b = Parameter[Matrix2D[Float]](
    name = "b",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val c = Parameter[Matrix2D[Float]](
    name = "c",
    mode = ReadWrite,
    storage = DeviceStorage
  )

  val prototype = Prototype(a,b,c)

  def makeKernelParams(params:Seq[MetaView],memory:MemoryNode):Seq[Any] = {
    val n = params(a).width.toInt
    val b1 = params(a).viewIn(memory).get.buffer
    val b2 = params(b).viewIn(memory).get.buffer
    val b3 = params(c).viewIn(memory).get.buffer
    Seq(n,b1,b2,b3)
  }
}


object FloatMatrixMultiplication extends Function {
  val peer = FloatMatrixMultiplicationMetaKernel

  def createTask(args:Seq[FutureData]):FutureEvent[Task] = args.map(_.data) match {
    case Seq(a:Matrix2D[_], b:Matrix2D[_]) => {
      val c = new Matrix2D[Float](b.width, a.height)
      val task = Task(peer, List(a,b,c), c)
      FutureEvent(task)
    }
    case _ => throw new Exception("Invalid parameters: "+args)
  }
}
