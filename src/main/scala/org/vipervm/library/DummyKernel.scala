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

class DummyOpenCLKernel extends OpenCLKernel {
  val source = """
    __kernel void dummy(__global float * in, __global float * out, int a) {
      int i = get_global_id(0);
      out[i] = in[i] * (float)a;
      out[i] = 1024.0;
    }
  """

  val program = new OpenCLProgram(source)

  val in = Parameter[Buffer](
    name = "in",
    mode = ReadOnly,
    storage = DeviceStorage
  )
  val out = Parameter[Buffer](
    name = "out",
    mode = ReadWrite,
    storage = DeviceStorage
  )
  val factor = Parameter[Int](
    name = "factor",
    mode = ReadOnly,
    storage = HostStorage
  )
  val size = Parameter[Long](
    name = "size",
    mode = ReadOnly,
    storage = HostStorage
  )

  val prototype = Prototype(in,out,factor,size)

  def configure(device:OpenCLProcessor, params:Seq[Any]) = {

    val config = OpenCLKernelConfig(
      kernelName = "dummy",
      globalWorkSize = List(params(size), 1, 1),
      parameters = IndexedSeq(params(in), params(out), params(factor))
    )

    Some(config)
  }
}
