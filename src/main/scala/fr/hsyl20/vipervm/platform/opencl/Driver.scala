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

package fr.hsyl20.vipervm.platform.opencl

import fr.hsyl20.vipervm.platform._
import fr.hsyl20.{opencl => cl}

/**
 * OpenCL platform driver
 *
 * @param enableGPUs Enable GPU devices
 * @param enableCPUs Enable CPU devices
 * @param enableAccelerators Enable accelerator devices
 */
class OpenCLDriver(
    enableGPUs:Boolean = true,
    enableCPUs:Boolean = false,
    enableAccelerators:Boolean = true)
  extends Driver {

  implicit def richBoolean(b:Boolean) = new {
    def apply[A](t:A,f:A):A = if (b) t else f
  }
   
  val processors:Seq[OpenCLProcessor] = {
    import cl.Device._
    val enabledDeviceTypes = enableGPUs(CL_DEVICE_TYPE_GPU,0) |
                       enableCPUs(CL_DEVICE_TYPE_CPU,0) |
                       enableAccelerators(CL_DEVICE_TYPE_ACCELERATOR,0)

    cl.OpenCL.platforms.flatMap(_.devices(enabledDeviceTypes)).map(new OpenCLProcessor(_))
  }

  val networks:Seq[OpenCLNetwork] = processors.map(new OpenCLNetwork(_))

  val memories:Seq[OpenCLMemoryNode] = processors.map(new OpenCLMemoryNode(_))

}
