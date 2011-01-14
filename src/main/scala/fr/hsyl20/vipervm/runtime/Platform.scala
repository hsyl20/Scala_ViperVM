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
class Platform(val drivers:Driver*) {
   
  /**
   * Memory nodes
   */
  def memories: Seq[MemoryNode] = drivers.flatMap(_.memories)

  /**
   * Networks
   */
  def networks: Seq[Network] = drivers.flatMap(_.networks)

  /**
   * Processors
   */
  def processors: Seq[Processor] = drivers.flatMap(_.processors)
}
