/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

import org.junit._

<<<<<<< HEAD
/*import fr.hsyl20.vipervm.runtime._
=======
import fr.hsyl20.vipervm.runtime._
>>>>>>> Work in progress
import fr.hsyl20.vipervm.runtime.opencl.OpenCLDriver
import fr.hsyl20.vipervm.runtime.schedulers.SingleDeviceScheduler
import fr.hsyl20.vipervm.runtime.dataschedulers.DefaultDataScheduler

class TaskSchedule {

<<<<<<< HEAD
  @Test
=======
/*  @Test
>>>>>>> Work in progress
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

<<<<<<< HEAD
  }
}*/
=======
  }*/
}
>>>>>>> Work in progress

