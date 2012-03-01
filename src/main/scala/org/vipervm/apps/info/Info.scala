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
import org.vipervm.platform.jvm.JVMDriver
import org.vipervm.platform.opencl.OpenCLDriver
import org.vipervm.platform.host.DefaultHostDriver

/**
 * Application showing informations about the platform (available devices, etc.)
 */
object Info {

  def main(args:Array[String]): Unit = {
    val host = DefaultHostDriver
    val platform = Platform(host, new OpenCLDriver, new JVMDriver(host))

    println("Processors:")
    platform.processors.foreach(a => println(" - %s".format(a)))

    platform.shutdown
  }
}
