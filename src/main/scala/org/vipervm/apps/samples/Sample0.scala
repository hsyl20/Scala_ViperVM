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

package org.vipervm.apps.samples

import org.vipervm.platform.Platform
import org.vipervm.platform.opencl.OpenCLDriver
import org.vipervm.platform.host.DefaultHostDriver

import org.vipervm.runtime.Runtime
import org.vipervm.runtime.Program
import org.vipervm.runtime.Data

import org.vipervm.runtime.ast.DataTerm
import org.vipervm.data.Matrix


object Sample0 {

  implicit def d2dt(data:Data):DataTerm = new DataTerm(data)

  def main(args:Array[String]): Unit = {
    val d0 = Matrix.random[Float](1024, 2048)
    val d1 = Matrix.random[Float](1024, 2048)
    val d2 = Matrix.random[Float](2048, 512)

    val source = "def my_operation(m:Matrix, n:Matrix, f:Matrix): Matrix = (m + n) * f"

    val platform = new Platform(new DefaultHostDriver, new OpenCLDriver)
    implicit val runtime = new Runtime(platform)

    val program = new Program(source)
    
    val my_op = program("my_operation")
    
    val d3 = runtime.call(my_op,d0,d1,d2)

  }
}
