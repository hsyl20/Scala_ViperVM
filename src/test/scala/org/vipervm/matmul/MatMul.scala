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

import org.scalatest.FunSuite

import org.vipervm.platform.Platform
import org.vipervm.platform.opencl.OpenCLDriver
import org.vipervm.platform.host.DefaultHostDriver

import org.vipervm.runtime._
import org.vipervm.runtime.data._
import org.vipervm.runtime.scheduling.DefaultScheduler

import org.vipervm.runtime.interpreter._

import org.vipervm.library._

import org.vipervm.profiling.SLF4JProfiler

import org.vipervm.parsers.LispyParser

class MatMul extends FunSuite {

  test("R = A*B + A*C using AST") {
    val src = TmApp(TmKernel("matadd"), Vector(
      TmApp(TmKernel("matmul"), Vector(TmVar("a"), TmVar("b"))),
      TmApp(TmKernel("matmul"), Vector(TmVar("a"), TmVar("c")))))

    val a = new Matrix2D[Float](32,32)
    val b = new Matrix2D[Float](32,32)
    val c = new Matrix2D[Float](32,32)
    val matmul = new MatMulFunction
    val matadd = new MatAddFunction

    val symbols = SymbolTable(
      Map("a" -> a, "b" -> b, "c" -> c),
      Map("matmul" -> matmul, "matadd" -> matadd))

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    val program = Program(src,symbols)

    testMatMul(platform, program, a, b, c)
  }

  test("R = A*B + A*C using Lispy parser") {
    val src = LispyParser.parse("""
      (matadd 
        (matmul a b)
        (matmul a c))
      """)

    val a = new Matrix2D[Float](32,32)
    val b = new Matrix2D[Float](32,32)
    val c = new Matrix2D[Float](32,32)
    val matmul = new MatMulFunction
    val matadd = new MatAddFunction

    val symbols = SymbolTable(
      Map("a" -> a, "b" -> b, "c" -> c),
      Map("matmul" -> matmul, "matadd" -> matadd))

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    val program = Program(src.get,symbols)

    testMatMul(platform, program, a, b, c)
  }

  test("R = A*B + A*C using DSL") {
    import org.vipervm.dsl._
    val a = Matrix2D[Float](32,32)
    val b = Matrix2D[Float](32,32)
    val c = Matrix2D[Float](32,32)
    val program = a*b + a*c

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    testMatMul(platform, program, a.peer.get, b.peer.get, c.peer.get)
  }


  private def testMatMul(platform:Platform, program:Program, a:Matrix2D[Float], b:Matrix2D[Float], c:Matrix2D[Float]):Unit = {
    a.initialize(platform, (x,y) => if (x == y) 1.0f else 0.0f )
    b.initialize(platform, (x,y) => 2.0f )
    c.initialize(platform, (x,y) => 2.0f )

    val profiler = new SLF4JProfiler
    val sched = new DefaultScheduler(platform,profiler)
    val interp = new Interpreter(sched)

    val result = interp.evaluate(program)

    result.syncWait

    val r = result.data.asInstanceOf[Matrix2D[Float]]
    
    println(r.print(platform)())
  }

}
