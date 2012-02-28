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
import org.vipervm.runtime.scheduling._
import org.vipervm.runtime.mm._
import org.vipervm.runtime.interpreter._

import org.vipervm.library._

import org.vipervm.parsers.LispyParser

class TestPolicies extends FunSuite {

  test("R = A*B + A*C using DefaultScheduler") {
    val platform = Platform(DefaultHostDriver, new OpenCLDriver)
    val dataManager = DefaultDataManager(platform)
    val sched = new DefaultScheduler(dataManager)

    testMatMul(platform, dataManager, sched)
  }

  test("R = A*B + A*C using DataAffinity scheduler policy") {
    val platform = Platform(DefaultHostDriver, new OpenCLDriver)
    val dataManager = DefaultDataManager(platform)
    val sched = new DefaultScheduler(dataManager) with DataAffinityPolicy

    testMatMul(platform, dataManager, sched)
  }


  private def testMatMul(platform:Platform, dataManager:DataManager, sched:Scheduler):Unit = {
    import org.vipervm.dsl._
    val a = Matrix2D[Float](32,32)
    val b = Matrix2D[Float](32,32)
    val c = Matrix2D[Float](32,32)
    val program = a*b + a*c

    a.peer.get.initialize(dataManager, (x,y) => if (x == y) 1.0f else 0.0f )
    b.peer.get.initialize(dataManager, (x,y) => 2.0f )
    c.peer.get.initialize(dataManager, (x,y) => 2.0f )

    val interp = new Interpreter(sched)

    val result = interp.evaluate(program)

    result.syncWait
  }

}
