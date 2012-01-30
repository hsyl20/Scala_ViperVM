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

package org.vipervm.tests.runtime

import org.scalatest.FunSuite

import org.vipervm.platform.Platform
import org.vipervm.platform.opencl.OpenCLDriver
import org.vipervm.platform.host.DefaultHostDriver

import org.vipervm.runtime._
import org.vipervm.runtime.data._
import org.vipervm.runtime.scheduling.DefaultScheduler

class TestEngine extends FunSuite {

  test("Basic expression") {
    val a = new Matrix2D(4, 100, 50)
    val b = new Matrix2D(4, 50, 20)
    val c = new Matrix2D(4, 50, 20)
    val matmul = new FMatMulKernel
    val matadd = new FMatAddKernel

    val prog = TmApp(TmKernel("matadd"), Vector(
      TmApp(TmKernel("matmul"), Vector(TmData("a"), TmData("b"))),
      TmApp(TmKernel("matmul"), Vector(TmData("a"), TmData("c")))))

    val context = Context(
      Map("a" -> a, "b" -> b, "c" -> c),
      Map("matmul" -> matmul, "matadd" -> matadd))

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    val sched = new DefaultScheduler(platform)
    val engine = new Engine(sched)
    engine.evaluate(prog,context)
  }

}