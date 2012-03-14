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
import org.vipervm.library.linearalgebra.kernels._

import org.vipervm.platform.{ReadOnly,WriteOnly}
import org.vipervm.runtime._
import org.vipervm.runtime.mm._

object FloatMatrixMultiplicationMetaKernel extends KernelSet {
  val kernels = Seq(FloatMatrixMultiplicationOpenCL,FloatMatrixMultiplicationJVM)
  
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
    val wA = params(a).meta.width
    val hA = params(a).meta.height
    val wB = params(b).meta.width
    val b1 = params(a).storage.view.buffer
    val b2 = params(b).storage.view.buffer
    val b3 = params(c).storage.view.buffer
    Seq(b1,b2,b3,wA,hA,wB)
  }
}
