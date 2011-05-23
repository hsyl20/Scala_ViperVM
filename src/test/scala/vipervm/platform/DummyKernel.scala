/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.tests.platform

import org.vipervm.platform.opencl._
import org.vipervm.platform.host._
import org.vipervm.platform._
import org.vipervm.bindings.opencl.OpenCLBuildProgramException

private[platform] class DummyKernel extends OpenCLKernel {
  val source = """__kernel void dummy(__global float * in, __global float * out, int a) {
                    int i = get_global_id(0);
                    out[i] = in[i] * (float)a;
                    out[i] = 1024.0;
                  }"""

  val program = new OpenCLProgram(source)
  val name = "dummy"

  val param_modes:Array[AccessMode] = Array(ReadOnly, ReadOnly, ReadWrite, ReadOnly)

  def configure(device:OpenCLProcessor, params:Seq[KernelParameter]) = params match {
    case LongKernelParameter(size) :: BufferKernelParameter(in) :: BufferKernelParameter(out) :: IntKernelParameter(factor) :: Nil => Some(new OpenCLKernelConfig {
      val globalWorkSize = List(size, 1, 1)
      val parameters = IndexedSeq(BufferKernelParameter(in), BufferKernelParameter(out), IntKernelParameter(factor))
    })
    case _ => None
  }
}
