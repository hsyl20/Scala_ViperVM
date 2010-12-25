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

import fr.hsyl20.auratune.runtime.AccessMode._

/**
 * Group of Data to access with the specified access mode.
 *
 * Support memory node constraints (include, exclude) and piorities
 *
 * @param data Data and their associated access modes
 */
class DataConfig(val data:List[(Data,AccessMode)]) extends AbstractDataConfig {

  def this(data:(Data,AccessMode)*) = this(data.toList)

  /**
   * if "included" is not Nil, the DataState must only be
   * configured on a memory node contained in it. Otherwise any
   * memory node in the platform can be used.
   */
  val included:Seq[MemoryNode] = Nil

  /**
   * Memory nodes that must not be used
   */
  val excluded:Seq[MemoryNode] = Nil

  /**
   * Memory node priorities. 0 is default. Negative is lower priority and positive is higher priority
   */
  val priorities: Map[MemoryNode, Int] = Map.empty
}
