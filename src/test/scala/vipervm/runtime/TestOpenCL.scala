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

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.vipervm.platform.opencl._
import fr.hsyl20.vipervm.platform.host._
import fr.hsyl20.vipervm.platform._
import fr.hsyl20.opencl.OpenCLBuildProgramException

private class DummyKernel extends OpenCLKernel {
  val source = """__kernel void dummy(__global float * in, __global float * out, int a) {
                    int i = get_global_id(0);
                    out[i] = in[i] * a;
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

class OpenCLKernelSpec extends FlatSpec with ShouldMatchers {

  "A OpenCL kernel" should "be instantiable" in {


    /* Initialize a platform using OpenCL */
    val platform = new Platform(new DefaultHostDriver, new OpenCLDriver)

    /* Create a dummy kernel */
    val kernel = new DummyKernel

    /* Select an OpenCL processor */
    val device = platform.processors.filter(_.isInstanceOf[OpenCLProcessor]).headOption
    val proc = device match {
      case None => throw new Exception("No OpenCL device available")
      case Some(proc) => proc
    }

    /* Select a memory in which the processor can compute */
    val mem = proc.memory

    val n:Long = 100
    val factor = 10
    val in = mem.allocate(n * 4)
    val out = mem.allocate(n * 4)
    val params = Seq(LongKernelParameter(n), BufferKernelParameter(in), BufferKernelParameter(out), IntKernelParameter(factor))

    try {
      val event = proc.execute(kernel,params)
      event.syncWait
    }
    catch {
      case e@OpenCLBuildProgramException(err,program,devices) =>
        devices.foreach(dev => println(e.buildInfo(dev).log))
    }
  }

}
