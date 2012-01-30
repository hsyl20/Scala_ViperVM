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
import org.vipervm.runtime._
import org.vipervm.runtime.data.Matrix2D

class FMatMulKernel extends FunctionalKernel {
  val peer = new MatMulKernel

  def createTask(args:Seq[TaskParameter]):(Task,Data) = args match {
    case (a:Matrix2D) :: (b:Matrix2D) :: Nil => {
      val c = new Matrix2D(a.elemSize, b.width, a.height)
      val task = Task(peer, List(a,b,DataTaskParameter(c)))
      (task,c)
    }
    case _ => throw new Exception("Invalid parameters")
  }
}
