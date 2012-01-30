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

package org.vipervm.tests.lowlevel

import org.vipervm.platform.opencl._
import org.vipervm.platform.host._
import org.vipervm.platform._
import org.vipervm.bindings.opencl.OpenCLBuildProgramException

class MatAddKernel extends OpenCLKernel {
  val source = """
    __kernel void matrixAdd(
       const int width,
       const int height,
       __global float* A,
       __global float* B, 
       __global float* C) {
        int gx = get_global_id(0);
        int gy = get_global_id(1);

	if (gx < width && gy < height) {
	  C[gy*width+gx] = A[gy*width+gx] + B[gy*width+gx];
	}

    }
  """

  val program = new OpenCLProgram(source)
  val name = "matrixAdd"

  val width = Param[IntKernelParameter](0, ReadOnly)
  val height = Param[IntKernelParameter](1, ReadOnly)
  val a = Param[BufferKernelParameter](2, ReadOnly)
  val b = Param[BufferKernelParameter](3, ReadOnly)
  val c = Param[BufferKernelParameter](4, ReadWrite)

  def configure(device:OpenCLProcessor, params:Seq[KernelParameter]) = {

    val config = OpenCLKernelConfig(
      globalWorkSize = List(width(params).value, height(params).value, 1),
      localWorkSize = None,
      parameters = IndexedSeq(width(params),height(params), a(params), b(params), c(params))
    )

    Some(config)
  }
}
