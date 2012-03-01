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

package org.vipervm.tests

import org.scalatest.fixture.FixtureFunSuite

import org.vipervm.platform.opencl._
import org.vipervm.platform.host._
import org.vipervm.platform._
import org.vipervm.bindings.opencl.OpenCLBuildProgramException

import org.vipervm.platform.FutureEvent._
import org.vipervm.platform.Barrier._

import org.vipervm.library._

import java.util.Random
import scala.util.continuations._


class OpenCL extends FixtureFunSuite {

  type FixtureParam = Common

  def withFixture(test: OneArgTest) {
    val common = new Common
    try {
      test(common)
    }
    finally {
      import common._
      mem.free(inBuf)
      mem.free(outBuf)

      hostMem.free(hostBuf)
      hostMem.free(hostOutBuf)
    }
  }


  class Common {
    val n = 100L
    val factor = 10
    val kernel = new DummyOpenCLKernel

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    val device = platform.processors.filter(_.isInstanceOf[OpenCLProcessor]).headOption
    val proc = device.getOrElse {
      throw new Exception("No OpenCL device available")
    }

    try {
        proc.compile(kernel)
    }
    catch {
      case e@OpenCLBuildProgramException(err,program,devices) => {
        devices.foreach(dev => println(e.buildInfo(dev).log))
        throw e
      }
    }

    /* Select a memory in which the processor can compute */
    val mem = proc.memory

    val inBuf = mem.allocate(n * 4)
    val outBuf = mem.allocate(n * 4)

    val hostMem = platform.hostMemory
    val hostBuf = hostMem.allocate(n * 4)
    val hostOutBuf = hostMem.allocate(n * 4)

    val rand = new Random
    val fb = hostBuf.peer
    for (i <- 0L until n) {
      fb.setFloat(i*4, rand.nextFloat)
    }

    val writeLink = platform.linkBetween(hostBuf, inBuf).getOrElse {
      throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
    }

    val hostView = BufferView1D(hostBuf, 0, n * 4L)
    val hostOutView = BufferView1D(hostOutBuf, 0, n * 4L)
    val inView = BufferView1D(inBuf, 0, n * 4L)
    val outView = BufferView1D(outBuf, 0, n * 4L)

    val params = Seq(inBuf, outBuf, factor, n)

    val readLink = platform.linkBetween(outBuf, hostOutBuf).getOrElse {
      throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
    }

    def check:Boolean = {
      val fbout = hostOutBuf.peer

      var chk = true
      for (i <- 0L until n) {
        val a = factor * fb.getFloat(i*4)
        val b = fbout.getFloat(i*4)
        if (a - b > 0.001) {
          println("Invalid value computed: %f vs %f".format(a,b))
          chk = false
        }
      }
      chk
    }

  }


  test("Low-Level Synchronous OpenCL") { common => 
    import common._

    val writeEvent = writeLink.copy(hostView,inView)
    writeEvent.syncWait

    val event = proc.execute(kernel,params)
    event.syncWait

    val readEvent = readLink.copy(outView,hostOutView)
    readEvent.syncWait

    if (!check)
      throw new Exception("Test failure: invalid data retrieved")
  }


  test("Low-Level Asynchronous OpenCL") { common => 
    import common._

    val writeEvent = writeLink.copy(hostView,inView)
    val chk = writeEvent fold {

      val event = proc.execute(kernel,params)
      event fold {

        val readEvent = readLink.copy(outView,hostOutView)
        readEvent fold {
          check
        }
      }
    }

    val c = chk()()()
    if (!c)
      throw new Exception("Test failure: invalid data retrieved")
  }


  test("Low-Level Asynchronous OpenCL Using Continuations") { common =>
    import common._

    val chk = reset {
      /* Type of the returned value */
      type R = Boolean 

      val writeEvent = writeLink.copy(hostView,inView)
      barrier[R](writeEvent)

      val event = proc.execute(kernel,params)
      barrier[R](event)

      val readEvent = readLink.copy(outView,hostOutView)
      barrier[R](readEvent)

      FutureEvent(check)
    }

    if (!chk())
      throw new Exception("Test failure: invalid data retrieved")
  }
}
