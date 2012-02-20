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


class MatMulOpenCL extends FixtureFunSuite {

  type FixtureParam = Common

  def withFixture(test: OneArgTest) {
    val common = new Common{}
    try {
      test(common)
    }
    finally {
      import common._
      mem.free(aBuf)
      mem.free(bBuf)
      mem.free(cBuf)

      hostMem.free(hostaBuf)
      hostMem.free(hostbBuf)
      hostMem.free(hostcBuf)
    }
  }


  trait Common {
    val n = 32
    val kernel = new MatMulKernel

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

    val aBuf = mem.allocate(n * n * 4)
    val bBuf = mem.allocate(n * n * 4)
    val cBuf = mem.allocate(n * n * 4)

    val hostMem = platform.hostMemory
    val hostaBuf = hostMem.allocate(n * n * 4)
    val hostbBuf = hostMem.allocate(n * n * 4)
    val hostcBuf = hostMem.allocate(n * n * 4)

    val fba = hostaBuf.peer
    val fbb = hostbBuf.peer
    val fbc = hostcBuf.peer

    val rand = new Random
    for (i <- 0 until (n*n)) {
      fba.setFloat(i*4, rand.nextFloat*10f)
      fbb.setFloat(i*4, rand.nextFloat*10f)
      fbc.setFloat(i*4, 0f)
    }

    val writeLink = platform.linkBetween(hostaBuf, aBuf).getOrElse {
      throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
    }

    val hostaView = BufferView1D(hostaBuf, 0, n * n * 4L)
    val hostbView = BufferView1D(hostbBuf, 0, n * n * 4L)
    val hostcView = BufferView1D(hostcBuf, 0, n * n * 4L)
    val aView = BufferView1D(aBuf, 0, n * n * 4L)
    val bView = BufferView1D(bBuf, 0, n * n * 4L)
    val cView = BufferView1D(cBuf, 0, n * n * 4L)

    val params = Seq(n, aBuf, bBuf, cBuf)

    val readLink = platform.linkBetween(aBuf, hostaBuf).getOrElse {
      throw new Exception("Transfer between host and OpenCL memory impossible. Link not available")
    }

    def check:Boolean = {
      var chk = true
      for (i <- 0 until n; j <- 0 until n) {
        val s = (for (k <- 0 until n) yield fba.getFloat((i*n+k)*4) * fbb.getFloat((k*n+j)*4)).sum
        val t = fbc.getFloat((j+i*n)*4)
        if (math.abs(s - t) > 0.001) {
          println("Invalid value computed: %f vs %f".format(s,t))
          chk = false
        }
      }
      chk
    }

  }


  test("Low-Level Synchronous OpenCL") { common => 
    import common._

    val writeEventA = writeLink.copy(hostaView,aView)
    val writeEventB = writeLink.copy(hostbView,bView)
    EventGroup(writeEventA, writeEventB).syncWait

    val event = proc.execute(kernel,params)
    event.syncWait

    val readEvent = readLink.copy(cView,hostcView)
    readEvent.syncWait

    if (!check)
      throw new Exception("Test failure: invalid data retrieved")
  }


  test("Low-Level Asynchronous OpenCL") { common => 
    import common._

    val writeEventA = writeLink.copy(hostaView,aView)
    val writeEventB = writeLink.copy(hostbView,bView)

    val chk = EventGroup(writeEventA, writeEventB).fold {

      val event = proc.execute(kernel,params)
      event fold {

        val readEvent = readLink.copy(cView,hostcView)
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

      val writeEventA = writeLink.copy(hostaView,aView)
      val writeEventB = writeLink.copy(hostbView,bView)
      barrier[R](EventGroup(writeEventA, writeEventB))

      val event = proc.execute(kernel,params)
      barrier[R](event)

      val readEvent = readLink.copy(cView,hostcView)
      barrier[R](readEvent)

      FutureEvent(check)
    }

    if (!chk())
      throw new Exception("Test failure: invalid data retrieved")
  }
}
