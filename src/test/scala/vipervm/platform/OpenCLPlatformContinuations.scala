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

package org.vipervm.tests.platform

import org.scalatest.FunSuite

import org.vipervm.platform.opencl._
import org.vipervm.platform.host._
import org.vipervm.platform._
import org.vipervm.bindings.opencl.OpenCLBuildProgramException

import org.vipervm.platform.FutureEvent._
import scala.util.continuations._

import java.util.Random

class OpenCLPlatformContinuations extends FunSuite with Sequence {

  val n = 100
  val factor = 10

  test("continuation plugin for asynchronous OpenCL barriers") {

    val check = reset {
      val platform = new Platform(new DefaultHostDriver, new OpenCLDriver)

      val device = platform.processors.filter(_.isInstanceOf[OpenCLProcessor]).headOption
      val proc = device match {
        case None => throw new Exception("No OpenCL device available")
        case Some(proc) => proc
      }

      /* Select a memory in which the processor can compute */
      val mem = proc.memory

      val inBuf = mem.allocate(n * 4)
      val outBuf = mem.allocate(n * 4)

      val hostMem = platform.hostMemory
      val hostBuf = hostMem.allocate(n * 4)
      val hostOutBuf = hostMem.allocate(n * 4)

      val rand = new Random
      val fb = hostBuf.byteBuffer.asFloatBuffer; 
      for (i <- 0 until n) {
        fb.put(i, rand.nextFloat)
      }

      val writeLink = platform.linkBetween(hostBuf, inBuf) match {
        case None => throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
        case Some(l) => l
      }

      val hostView = BufferView1D(hostBuf, 0, n * 4L)
      val hostOutView = BufferView1D(hostOutBuf, 0, n * 4L)
      val inView = BufferView1D(inBuf, 0, n * 4L)
      val outView = BufferView1D(outBuf, 0, n * 4L)

      val writeEvent = writeLink.copy(hostView,inView)

      barrier[Boolean](writeEvent)

      val kernel = new DummyKernel

      val params = Seq(BufferKernelParameter(inBuf), BufferKernelParameter(outBuf), IntKernelParameter(factor), LongKernelParameter(n))

      /*val event = try {
        proc.execute(kernel,params)
      }
      catch {
        case e@OpenCLBuildProgramException(err,program,devices) => {
          devices.foreach(dev => println(e.buildInfo(dev).log))
          throw e
        }
        case e => throw e
      }*/
      val event = proc.execute(kernel,params)

      barrier[Boolean](event)

      val readLink = platform.linkBetween(outBuf, hostOutBuf) match {
        case None => throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
        case Some(l) => l
      }

      val readEvent = readLink.copy(outView,hostOutView)

      barrier[Boolean](readEvent)

      mem.free(inBuf)
      mem.free(outBuf)

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

      hostMem.free(hostBuf)
      hostMem.free(hostOutBuf)

      constant(check)
    }

    if (!check())
      throw new Exception("Test failure: retrieved data invalid")

  }
}
