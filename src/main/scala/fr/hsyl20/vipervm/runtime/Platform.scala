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

package fr.hsyl20.vipervm.runtime

/** This class gives a unified representation of the runtime platform.
 *
 * Drivers are to be registered into the platform to be used
 */
class Platform {
   
  private var drivers:List[Driver] = Nil

  /**
   * Register a driver
   *
   * Devices that can be managed by this driver may be used
   */
  def registerDriver(driver:Driver): Unit = {
    if (!drivers.contains(driver))
      drivers = driver :: drivers
  }

  /**
   * Available devices
   */
  def devices: Seq[Device] = drivers.flatMap(_.devices)

  /**
   * Memory nodes
   */
  def memoryNodes: Seq[MemoryNode] = devices.flatMap(_.memoryNodes).distinct
}


object Platform {

  /**
   * Create a Platform and register given drivers
   */
  def apply(drivers:Driver*) = {
    val p = new Platform
    for (d <- drivers)
      p.registerDriver(d)
    p
  }

}
