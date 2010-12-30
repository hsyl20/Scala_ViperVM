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

/**
 * Event triggered when a given data configuration is obtained
 *
 * You should release the associated DataConfig as soon as possible,
 * otherwise it will be released on Garbage Collection
 */
trait DataConfigEvent extends Event {

  /** Return the associated DataState */
  def config: DataConfig

  /**
   * Selected memory node (only available after event completion)
   */
  def memoryNode: MemoryNode

  override def finalize: Unit = {
    config.release
    super.finalize
  }
}
