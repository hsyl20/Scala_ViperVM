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

import org.vipervm.library.linearalgebra.kernels.jvm.FloatMatrixAdditionJVM
import org.vipervm.library.linearalgebra.kernels.opencl.FloatMatrixAdditionOpenCL
import org.vipervm.library.linearalgebra.kernels._

import org.vipervm.platform.{ReadOnly,WriteOnly}
import org.vipervm.runtime._
import org.vipervm.runtime.mm._

object FloatMatrixAdditionMetaKernel extends KernelSet {

  val kernels = Seq(FloatMatrixAdditionOpenCL,FloatMatrixAdditionJVM)

  val a = Parameter[DenseMatrixInstance](
    typ = MatrixType(FloatType),
    repr = DenseMatrixRepr,
    name = "a",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val b = Parameter[DenseMatrixInstance](
    typ = MatrixType(FloatType),
    repr = DenseMatrixRepr,
    name = "b",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val c = Parameter[DenseMatrixInstance](
    typ = MatrixType(FloatType),
    repr = DenseMatrixRepr,
    name = "c",
    mode = WriteOnly,
    storage = DeviceStorage
  )

  val prototype = Prototype(a,b,c)

  def makeKernelParams(params:Seq[DataInstance]):Seq[Any] = {
    val w = params(a).meta.width
    val h = params(a).meta.height
    val b1 = params(a).storage.view.buffer
    val b2 = params(b).storage.view.buffer
    val b3 = params(c).storage.view.buffer
    Seq(w,h,b1,b2,b3)
  }
}

object FloatMatrixAdditionFunction extends Function {
  val prototype = MatrixAdditionProto
  val kernel = FloatMatrixAdditionMetaKernel
}
