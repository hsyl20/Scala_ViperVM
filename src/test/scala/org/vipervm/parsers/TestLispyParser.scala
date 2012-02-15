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

package org.vipervm.tests.parsers

import org.scalatest.FunSuite

import scala.util.Random
import org.vipervm.tests.runtime._

import org.vipervm.platform.{Platform,HostBuffer,BufferView2D}
import org.vipervm.platform.opencl.OpenCLDriver
import org.vipervm.platform.host.DefaultHostDriver

import org.vipervm.runtime._
import org.vipervm.runtime.data._
import org.vipervm.runtime.scheduling.DefaultScheduler

import org.vipervm.parser.LispyParser

class TestLispyParser extends FunSuite {

  test("Basic expression") {
    val a = new Matrix2D[Float](32,32)
    val b = new Matrix2D[Float](32,32)
    val c = new Matrix2D[Float](32,32)
    val matmul = new FMatMulKernel
    val matadd = new FMatAddKernel

    val context = Context(
      Map("a" -> DataValue(a), "b" -> DataValue(b), "c" -> DataValue(c)),
      Map("matmul" -> matmul, "matadd" -> matadd))

    val platform = Platform(DefaultHostDriver, new OpenCLDriver)

    val sched = new DefaultScheduler(platform)
    val engine = new Engine(sched)

    val prog = LispyParser.parse("""
      (matadd 
        (matmul a b)
        (matmul a c))
      """)

    a.initialize(platform, (x,y) => if (x == y) 1.0f else 0.0f )
    b.initialize(platform, (x,y) => 2.0f )
    c.initialize(platform, (x,y) => 2.0f )

    val result = engine.evaluate(prog.get,context)

    result.syncWait

    val r = result.value match {
      case DataValue(d) => d
      case _ => throw new Exception("Invalid value returned")
    }
    
    println(r.asInstanceOf[Matrix2D[Float]].print(platform)())
  }

}
