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
import org.vipervm.bindings.opencl.OpenCLBuildProgramException

class DummyKernel extends OpenCLKernel {
  val source = """
    __kernel void dummy(__global float * in, __global float * out, int a) {
      int i = get_global_id(0);
      out[i] = in[i] * (float)a;
      out[i] = 1024.0;
    }
  """

  val program = new OpenCLProgram(source)
  val name = "dummy"

  val in = Param[BufferKernelParameter](0, ReadOnly)
  val out = Param[BufferKernelParameter](1, ReadWrite)
  val factor = Param[IntKernelParameter](2, ReadOnly)
  val size = Param[LongKernelParameter](3, ReadOnly)

  def configure(device:OpenCLProcessor, params:Seq[KernelParameter]) = {

    val config = OpenCLKernelConfig(
      globalWorkSize = List(size(params).value, 1, 1),
      parameters = IndexedSeq(in(params), out(params), factor(params))
    )

    Some(config)
  }
}
