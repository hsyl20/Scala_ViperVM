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

import org.junit._

/*import org.vipervm.runtime._
import org.vipervm.runtime.opencl.OpenCLDriver
import org.vipervm.runtime.schedulers.SingleDeviceScheduler
import org.vipervm.runtime.dataschedulers.DefaultDataScheduler

class TaskSchedule {

  @Test
  def test() = {

    val r = new Runtime {
      val platform = Platform(new OpenCLDriver)
      val dataScheduler = new DefaultDataScheduler(this)
      val taskScheduler = new SingleDeviceScheduler(platform.devices.head, this)
    }

    val clk = new OpenCLKernel {
    }

    val ks = new KernelSet(
        KernelParameterDecl(BufferParameterType,ReadOnly),
        KernelParameterDecl(BufferParameterType,WriteOnly)
      ){
        kernels += clk
    }

    val t = new Task(clk)

    val d1 = Matrix.allocate(100,100)(r)
    val d2 = Matrix.allocate(100,100)(r)

    r.schedule(t,d1,d2)

  }
}*/

