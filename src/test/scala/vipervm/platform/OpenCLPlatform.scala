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

import java.util.Random

private class DummyKernel extends OpenCLKernel {
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

class OpenCLKernelSpec extends FlatSpec with ShouldMatchers {

  "A OpenCL kernel" should "be instantiable" in {


    /* Initialize a platform using OpenCL */
    val platform = new Platform(new DefaultHostDriver, new OpenCLDriver)

    /* Kernel parameters */
    val n = 100
    val factor = 10

    /* Select an OpenCL processor */
    val device = platform.processors.filter(_.isInstanceOf[OpenCLProcessor]).headOption
    val proc = device match {
      case None => throw new Exception("No OpenCL device available")
      case Some(proc) => proc
    }

    /* Select a memory in which the processor can compute */
    val mem = proc.memory

    /* Allocate buffer in device memory */
    val inBuf = mem.allocate(n * 4)
    val outBuf = mem.allocate(n * 4)

    /* Allocate and initialize host buffer */
    val hostBuf = platform.hostMemory.allocate(n * 4)
    val hostOutBuf = platform.hostMemory.allocate(n * 4)

    val rand = new Random
    val fb = hostBuf.byteBuffer.asFloatBuffer; 
    for (i <- 0 until n) {
      fb.put(i, rand.nextFloat)
    }

    /* Get write link */
    val writeLink = platform.linkBetween(hostBuf, inBuf) match {
      case None => throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
      case Some(l) => l
    }

    /* Create views */
    val hostView = BufferView1D(hostBuf, 0, n * 4L)
    val hostOutView = BufferView1D(hostOutBuf, 0, n * 4L)
    val inView = BufferView1D(inBuf, 0, n * 4L)
    val outView = BufferView1D(outBuf, 0, n * 4L)

    /* Copy data from host memory to device memory */
    val writeEvent = writeLink.copy(hostView,inView)
    writeEvent.syncWait

    /* Create a dummy kernel */
    val kernel = new DummyKernel

    /* Kernel parameters */
    val params = Seq(LongKernelParameter(n), BufferKernelParameter(inBuf), BufferKernelParameter(outBuf), IntKernelParameter(factor))

    /* Execute kernel */
    val event = try {
      proc.execute(kernel,params)
    }
    catch {
      case e@OpenCLBuildProgramException(err,program,devices) => {
        devices.foreach(dev => println(e.buildInfo(dev).log))
        throw e
      }
      case e => throw e
    }

    /* Wait for kernel completion */
    event.syncWait

    /* Get read link */
    val readLink = platform.linkBetween(outBuf, hostOutBuf) match {
      case None => throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
      case Some(l) => l
    }

    /* Copy data from device memory to host memory */
    val readEvent = readLink.copy(outView,hostOutView)
    readEvent.syncWait

    /* Check data */
    val fbout = hostOutBuf.byteBuffer.asFloatBuffer; 

    var check = true
    for (i <- 0 until n) {
      val a = factor * fb.get(i)
      val b = fbout.get(i)
      if (a - b > 0.001) {
        println("Invalid value computed: %f vs %f".format(a,b))
        check = false
      }
    }

    if (!check)
      throw new Exception("Test failure: invalid values")
  }

}
