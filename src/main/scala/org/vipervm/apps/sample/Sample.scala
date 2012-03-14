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

package org.vipervm.apps

import org.vipervm.platform.Platform
import org.vipervm.platform.opencl.OpenCLDriver
import org.vipervm.platform.jvm.JVMDriver
import org.vipervm.platform.host.DefaultHostDriver

import org.vipervm.runtime._
import org.vipervm.runtime.mm._
import org.vipervm.runtime.scheduling._
import org.vipervm.runtime.interpreter._

import org.vipervm.library._

import org.vipervm.parsers.LispyParser

import org.vipervm.profiling._

private class SampleApp(size:Long = 32) {

  val host = DefaultHostDriver
  //val platform = Platform(host, new OpenCLDriver, new JVMDriver(host))
  //val platform = Platform(host, new JVMDriver(host))
  val platform = Platform(host, new OpenCLDriver)
  val profiler = SVGProfiler(platform)
  val library = DefaultLibrary()
  implicit val runtime = DefaultRuntime(platform,library,profiler)
  val interp = new DefaultInterpreter(runtime)

  val frame = Profiler.dynamicRendering(profiler)

  import org.vipervm.dsl._
  val a = Matrix.create[Float](size,size) {
    (x,y) => if (x == y) 1.0f else 0.0f
  }
  val b = Matrix.create[Float](32L,size)((_,_) => 2.0f)
  val c = Matrix.create[Float](32L,size)((_,_) => 2.0f)

  def makeTree(init:MatrixDSL, height:Int):MatrixDSL = {
    (init /: (0 to height))((init,_) => init + init)
  }
//  val program = makeTree(a*b+a*c, 3)
//  val program = let (x -> a*b, y -> a*c) in (x+y) * (x+y)
  val program = a*b + a*c


  val result = interp.evaluate(program)

  if (size < 64) {
/*    val r = result.data.asInstanceOf[data.Matrix2D[Float]]
    println(r.print(dataManager)())*/
  }
  else {
    println("Printing disabled (size of the matrices too big)")
  }

  profiler.save("profile.svg")

  platform.shutdown
}


object Sample {
  def main(args:Array[String]):Unit = {
    if (args.length != 0) {
      new SampleApp(args(0).toInt)
    }
    else {
      new SampleApp
    }

  }
}
