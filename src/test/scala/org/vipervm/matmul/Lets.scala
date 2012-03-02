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
import org.vipervm.runtime.mm._
import org.vipervm.runtime.interpreter._

import org.vipervm.library._

import org.vipervm.profiling.SLF4JProfiler

import org.vipervm.parsers.LispyParser

class MatMulLets extends FunSuite {

  test("let x = a*b in x + a*c using AST") {
    val src = TmLet(TmId("x"),TmApp(TmId("*"), Vector(TmId("a"), TmId("b"))),
      TmApp(TmId("+"), Vector(
        TmId("x"),
        TmApp(TmId("*"), Vector(TmId("a"), TmId("c"))))))

    val a = new Matrix2D[Float](32,32)
    val b = new Matrix2D[Float](32,32)
    val c = new Matrix2D[Float](32,32)

    val symbols = SymbolTable(Map("a" -> a, "b" -> b, "c" -> c))

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    val program = Program(src,symbols)

    testMatMul(platform, program, a, b, c)
  }

  test("let x = a*b in x + a*c using Lispy parser") {
    val src = LispyParser.parse("""
      (let ((x (* a b)))
        (+
          x
          (* a c)))
      """)

    val a = new Matrix2D[Float](32,32)
    val b = new Matrix2D[Float](32,32)
    val c = new Matrix2D[Float](32,32)

    val symbols = SymbolTable( Map("a" -> a, "b" -> b, "c" -> c))

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    val program = Program(src.get,symbols)

    testMatMul(platform, program, a, b, c)
  }

  test("let x = a*b in x + a*c using DSL") {
    import org.vipervm.dsl._
    val a = Matrix2D[Float](32,32)
    val b = Matrix2D[Float](32,32)
    val c = Matrix2D[Float](32,32)
    val x = Matrix2D[Float](32,32)

    val program = let (x -> a*b) in x + a*c

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    testMatMul(platform, program, a.peer.get, b.peer.get, c.peer.get)
  }


  private def testMatMul(platform:Platform, program:Program, a:Matrix2D[Float], b:Matrix2D[Float], c:Matrix2D[Float]):Unit = {
    val profiler = SLF4JProfiler()
    val dataManager = DefaultDataManager(platform,profiler)

    a.initialize(dataManager, (x,y) => if (x == y) 1.0f else 0.0f )
    b.initialize(dataManager, (x,y) => 2.0f )
    c.initialize(dataManager, (x,y) => 2.0f )

    val sched = new DefaultScheduler(dataManager,profiler)
    val interp = new Interpreter(sched,DefaultLibrary())

    val result = interp.evaluate(program)

    result.syncWait

    val r = result.data.asInstanceOf[Matrix2D[Float]]
    
    println(r.print(dataManager)())

    platform.shutdown
  }

}
