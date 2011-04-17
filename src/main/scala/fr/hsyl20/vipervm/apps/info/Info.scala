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

package fr.hsyl20.vipervm.apps

import fr.hsyl20.vipervm.platform.Platform
import fr.hsyl20.vipervm.platform.opencl.OpenCLDriver
import fr.hsyl20.vipervm.platform.host.DefaultHostDriver

/**
 * Application showing informations about the platform (available devices, etc.)
 */
object Info {

  def main(args:Array[String]): Unit = {
    val p = new Platform(new DefaultHostDriver, new OpenCLDriver)

    println("Processors:")
    p.processors.foreach(a => println(" - %s".format(a)))
  }
}
