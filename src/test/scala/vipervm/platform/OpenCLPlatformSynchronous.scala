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

package fr.hsyl20.vipervm.tests.platform

import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen

import fr.hsyl20.vipervm.platform.opencl._
import fr.hsyl20.vipervm.platform.host._
import fr.hsyl20.vipervm.platform._
import fr.hsyl20.opencl.OpenCLBuildProgramException

import fr.hsyl20.vipervm.platform.FutureEvent._

import java.util.Random


class OpenCLPlatformSynchronous extends FeatureSpec with GivenWhenThen {

  val n = 100
  val factor = 10

  feature("The user can transfer data to an OpenCL device's memory, execute a kernel on it and bring the data back. ") {

    /*************************************************************
     * Synchronous execution
     *************************************************************/
    scenario("A synchronous wait is inserted between each command (write, execute, read)") {

      given("a platform with the OpenCLDriver enabled")
      val platform = new Platform(new DefaultHostDriver, new OpenCLDriver)

      given("an OpenCL device")
      val device = platform.processors.filter(_.isInstanceOf[OpenCLProcessor]).headOption
      val proc = device match {
        case None => throw new Exception("No OpenCL device available")
        case Some(proc) => proc
      }

      /* Select a memory in which the processor can compute */
      val mem = proc.memory


      then("buffers can be allocated in device memory")
      val inBuf = mem.allocate(n * 4)
      val outBuf = mem.allocate(n * 4)

      and("buffers can be allocated in host memory")
      val hostMem = platform.hostMemory
      val hostBuf = hostMem.allocate(n * 4)
      val hostOutBuf = hostMem.allocate(n * 4)

      and("host buffer can be filled with random data")
      val rand = new Random
      val fb = hostBuf.byteBuffer.asFloatBuffer; 
      for (i <- 0 until n) {
        fb.put(i, rand.nextFloat)
      }

      and("a link between from an host buffer to a device buffer can be found")
      val writeLink = platform.linkBetween(hostBuf, inBuf) match {
        case None => throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
        case Some(l) => l
      }

      and("1D views on host and device buffers can be created")
      val hostView = BufferView1D(hostBuf, 0, n * 4L)
      val hostOutView = BufferView1D(hostOutBuf, 0, n * 4L)
      val inView = BufferView1D(inBuf, 0, n * 4L)
      val outView = BufferView1D(outBuf, 0, n * 4L)

      and("a copy from host buffer to device buffer can be triggered")
      val writeEvent = writeLink.copy(hostView,inView)

      and("copy completion can be synchronously waited for")
      writeEvent.syncWait

      and("an OpenCL kernel object can be created")
      val kernel = new DummyKernel

      val params = Seq(LongKernelParameter(n), BufferKernelParameter(inBuf), BufferKernelParameter(outBuf), IntKernelParameter(factor))

      and("an OpenCL kernel object can be compiled and executed asynchonously")
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

      and("kernel execution completion can be synchronously waited for")
      event.syncWait

      and("a link between a device buffer and an host buffer can be found")
      val readLink = platform.linkBetween(outBuf, hostOutBuf) match {
        case None => throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
        case Some(l) => l
      }

      and("a copy from device buffer to host buffer can be triggered")
      val readEvent = readLink.copy(outView,hostOutView)

      and("copy completion can be synchronously waited for")
      readEvent.syncWait

      and("device buffers can be freed")
      mem.free(inBuf)
      mem.free(outBuf)

      and("retrieved data should be correct")
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
        throw new Exception("Test failure: retrieved data invalid")

      and("host buffer can be freed")
      hostMem.free(hostBuf)
      hostMem.free(hostOutBuf)
    }
  }
}
