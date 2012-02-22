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
import org.vipervm.platform.host.DefaultHostDriver

import org.vipervm.runtime._
import org.vipervm.runtime.data._
import org.vipervm.runtime.scheduling._
import org.vipervm.runtime.mm.DefaultDataManager
import org.vipervm.runtime.interpreter._

import org.vipervm.library._

import org.vipervm.parsers.LispyParser
import org.vipervm.dsl._

import org.vipervm.profiling._

private class SampleApp(size:Long = 32) {

  val platform = Platform(DefaultHostDriver, new OpenCLDriver)
  val profiler = new SVGProfiler(platform)
  val dataManager = new DefaultDataManager(platform,profiler)
  val sched = new DefaultScheduler(dataManager,profiler) with DataAffinityPolicy with LoadBalancingPolicy

  val frame = Profiler.dynamicRendering(profiler)

  val a = Matrix2D[Float](size,size)
  val b = Matrix2D[Float](size,size)
  val c = Matrix2D[Float](size,size)
  val program = a*b + a*c + b*c

  a.peer.get.initialize(platform, (x,y) => if (x == y) 1.0f else 0.0f )
  b.peer.get.initialize(platform, (x,y) => 2.0f )
  c.peer.get.initialize(platform, (x,y) => 2.0f )

  val interp = new Interpreter(sched)

  val result = interp.evaluate(program)

  result.syncWait

  if (size < 64) {
    val r = result.data.asInstanceOf[data.Matrix2D[Float]]
    println(r.print(dataManager)())
  }
  else {
    println("Printing disabled (size of the matrices too big)")
  }

  profiler.save("profile.svg")
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
