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

package org.vipervm.library.linearalgebra.kernels.opencl

import org.vipervm.library.linearalgebra.kernels.prototypes._
import org.vipervm.platform.opencl._

object FloatMatrixAdditionOpenCL extends OpenCLKernel with FloatMatrixAdditionPrototype {
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

  def configure(device:OpenCLProcessor, params:Seq[Any]) = {

    val config = OpenCLKernelConfig(
      kernelName = "matrixAdd",
      globalWorkSize = List(params(width), params(height), 1),
      localWorkSize = None,
      parameters = IndexedSeq(params(width),params(height), params(a), params(b), params(c))
    )

    Some(config)
  }
}
