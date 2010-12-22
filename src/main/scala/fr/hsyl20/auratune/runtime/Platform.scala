/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**                     GPLv3
*/

package fr.hsyl20.auratune.runtime

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
