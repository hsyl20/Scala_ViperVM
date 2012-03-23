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
import org.vipervm.runtime.mm._
import org.vipervm.runtime.interpreter._

import org.vipervm.library._

import org.vipervm.profiling.SLF4JProfiler

import org.vipervm.parsers.LispyParser

class MatMul extends FunSuite {

  test("R = A*B + A*C using AST") {
    val platform = Platform(DefaultHostDriver, new OpenCLDriver)
    val library = DefaultLibrary()
    val profiler = SLF4JProfiler()

    implicit val runtime = new DefaultRuntime(platform,library,profiler)

    val a = Matrix.create[Float](32,32)( (x,y) => if (x == y) 1.0f else 0.0f )
    val b = Matrix.create[Float](32,32)( (_,_) => 2.0f )
    val c = Matrix.create[Float](32,32)( (_,_) => 2.0f )

    val src = TmApp(TmId("+"), Seq(
      TmApp(TmId("*"), Seq(TmData(a.data), TmData(b.data))),
      TmApp(TmId("*"), Seq(TmData(a.data), TmData(c.data)))))

    testMatMul(runtime, src)
  }

  test("R = A*B + A*C using Lispy parser") {
    val platform = Platform(DefaultHostDriver, new OpenCLDriver)
    val library = DefaultLibrary()
    val profiler = SLF4JProfiler()

    implicit val runtime = new DefaultRuntime(platform,library,profiler)

    val a = Matrix.create[Float](32,32)( (x,y) => if (x == y) 1.0f else 0.0f )
    val b = Matrix.create[Float](32,32)( (_,_) => 2.0f )
    val c = Matrix.create[Float](32,32)( (_,_) => 2.0f )

    val parser = new LispyParser(
        "a" -> a.data,
        "b" -> b.data,
        "c" -> c.data
    )

    val src = parser.parse("""
      (+
        (* a b)
        (* a c))
      """)

    testMatMul(runtime, src.get)
  }

  test("R = A*B + A*C using DSL") {
    import org.vipervm.dsl._
    val platform = Platform(DefaultHostDriver, new OpenCLDriver)
    val library = DefaultLibrary()
    val profiler = SLF4JProfiler()

    implicit val runtime = new DefaultRuntime(platform,library,profiler)

    val a = Matrix.create[Float](32,32)( (x,y) => if (x == y) 1.0f else 0.0f )
    val b = Matrix.create[Float](32,32)( (_,_) => 2.0f )
    val c = Matrix.create[Float](32,32)( (_,_) => 2.0f )

    val program = a*b + a*c

    testMatMul(runtime, program.term)
  }


  private def testMatMul(runtime:Runtime, src:Term):Unit = {

    val interp = new DefaultInterpreter(runtime)
    println(interp.typeCheck(src))

    val ret = Matrix(interp.evaluate(src))

  /*  val result = interp.evaluate(program)

    result.syncWait

    val r = result.data.asInstanceOf[Matrix2D[Float]]
    
    println(r.print(dataManager)())*/

  }

}
