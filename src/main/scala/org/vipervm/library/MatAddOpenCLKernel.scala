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

import org.vipervm.platform.opencl._

object MatAddOpenCLKernel extends OpenCLKernel with MatAddKernelPrototype {
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

  def configure(device:OpenCLProcessor, params:Seq[Any]) = {

    val config = OpenCLKernelConfig(
      globalWorkSize = List(params(width), params(height), 1),
      localWorkSize = None,
      parameters = IndexedSeq(params(width),params(height), params(a), params(b), params(c))
    )

    Some(config)
  }
}
