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

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.vipervm.runtime._
import fr.hsyl20.vipervm.runtime.opencl.OpenCLDriver
import fr.hsyl20.vipervm.runtime.schedulers.SingleDeviceScheduler
import fr.hsyl20.vipervm.runtime.dataschedulers.DefaultDataScheduler

class RuntimeSpec extends FlatSpec with ShouldMatchers {

  "A runtime" should "be instantiable" in {

    val r = new Runtime {
      val platform = Platform(new OpenCLDriver)
      val dataScheduler = new DefaultDataScheduler(this)
      val taskScheduler = new SingleDeviceScheduler(platform.devices.head, this)
    }

  }
}
