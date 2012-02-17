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
import org.vipervm.platform.host._
import org.vipervm.platform._
import org.vipervm.platform.Parameter._
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

  val width = Parameter[Int](
    name = "width",
    mode = ReadOnly,
    storage = HostStorage,
    description = "Width of matrices"
  )
  val height = Parameter[Int](
    name = "height",
    mode = ReadOnly,
    storage = HostStorage,
    description = "Height of matrices"
  )
  val a = Parameter[Buffer](
    name = "a",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val b = Parameter[Buffer](
    name = "b",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val c = Parameter[Buffer](
    name = "c",
    mode = ReadWrite,
    storage = DeviceStorage
  )

  val prototype = Prototype(width,height,a,b,c)

  def configure(device:OpenCLProcessor, params:Seq[Any]) = {

    val config = OpenCLKernelConfig(
      globalWorkSize = List(params(width), params(height), 1),
      localWorkSize = None,
      parameters = IndexedSeq(params(width),params(height), params(a), params(b), params(c))
    )

    Some(config)
  }
}
