import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.auratune.runtime._
import fr.hsyl20.auratune.runtime.opencl.OpenCLDriver
import fr.hsyl20.auratune.runtime.schedulers.SingleDeviceScheduler
import fr.hsyl20.auratune.runtime.dataschedulers.DefaultDataScheduler

class RuntimeSpec extends FlatSpec with ShouldMatchers {

  "A runtime" should "be instantiable" in {

    val r = new Runtime {
      val platform = Platform(new OpenCLDriver)
      val dataScheduler = new DefaultDataScheduler(this)
      val taskScheduler = new SingleDeviceScheduler(platform.devices.head, this)
    }

  }
}
