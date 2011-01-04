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
   * Memory nodes
   */
  def memoryNodes: Seq[MemoryNode] = drivers.flatMap(_.memoryNodes)

  /**
   * Networks
   */
  def networks: Seq[Network] = drivers.flatMap(_.networks)

  /**
   * Retrieve processors that can work in given memory
   */
  def processorsFor(mem:MemoryNode):Seq[Processor] = drivers.flatMap(_.processors)
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
