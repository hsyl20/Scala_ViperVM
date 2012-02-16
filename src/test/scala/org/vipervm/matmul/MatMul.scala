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

import org.vipervm.parser.LispyParser

class TestMatMul extends FunSuite {

  test("R = A*B + A*C using AST") {
    val prog = TmApp(TmKernel("matadd"), Vector(
      TmApp(TmKernel("matmul"), Vector(TmVar("a"), TmVar("b"))),
      TmApp(TmKernel("matmul"), Vector(TmVar("a"), TmVar("c")))))

    val a = new Matrix2D[Float](32,32)
    val b = new Matrix2D[Float](32,32)
    val c = new Matrix2D[Float](32,32)
    val matmul = new FMatMulKernel
    val matadd = new FMatAddKernel

    val symbols = SymbolTable(
      Map("a" -> DataValue(a), "b" -> DataValue(b), "c" -> DataValue(c)),
      Map("matmul" -> matmul, "matadd" -> matadd))

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    testMatMul(platform,prog,symbols,a,b,c)
  }

  test("R = A*B + A*C using Lispy parser") {
    val prog = LispyParser.parse("""
      (matadd 
        (matmul a b)
        (matmul a c))
      """)

    val a = new Matrix2D[Float](32,32)
    val b = new Matrix2D[Float](32,32)
    val c = new Matrix2D[Float](32,32)
    val matmul = new FMatMulKernel
    val matadd = new FMatAddKernel

    val symbols = SymbolTable(
      Map("a" -> DataValue(a), "b" -> DataValue(b), "c" -> DataValue(c)),
      Map("matmul" -> matmul, "matadd" -> matadd))

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    testMatMul(platform,prog.get,symbols,a,b,c)
  }

  test("R = A*B + A*C using DSL") {
    import org.vipervm.dsl._
    val a = Matrix2D[Float](32,32)
    val b = Matrix2D[Float](32,32)
    val c = Matrix2D[Float](32,32)
    val prog = a*b + a*c

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    testMatMul(platform,prog.term, prog.symbols, a.peer.get, b.peer.get, c.peer.get)
  }


  private def testMatMul(platform:Platform, prog:Term, symbols:SymbolTable, a:Matrix2D[Float], b:Matrix2D[Float], c:Matrix2D[Float]):Unit = {
    a.initialize(platform, (x,y) => if (x == y) 1.0f else 0.0f )
    b.initialize(platform, (x,y) => 2.0f )
    c.initialize(platform, (x,y) => 2.0f )

    val sched = new DefaultScheduler(platform)
    val interp = new Interpreter(sched)

    val result = interp.evaluate(prog,symbols)

    result.syncWait

    val r = result.value match {
      case DataValue(d) => d
      case _ => throw new Exception("Invalid value returned")
    }
    
    println(r.asInstanceOf[Matrix2D[Float]].print(platform)())
  }

}