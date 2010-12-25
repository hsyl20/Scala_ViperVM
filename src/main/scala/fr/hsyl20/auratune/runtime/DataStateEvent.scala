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
 * You should release the associated DataState as soon as possible,
 * otherwise it will be released on Garbage Collection
 */
abstract class DataStateEvent extends Event {

  /** Return the associated DataState */
  def dataState: DataState

  /** Selected memory node */
  def memoryNode: MemoryNode

  override def finalize: Unit = {
    dataState.release
    super.finalize
  }
}
