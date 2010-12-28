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
