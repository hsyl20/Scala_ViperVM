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

import com.sun.jna.Memory

/**
 * A memory node
 */
abstract class MemoryNode {

  /* Devices associated to this node */
  val devices:Seq[Device]

  /**
   * Natural 2D padding
   */
  def padding2D(elemSize:Int,width:Long,height:Long): Long = 4
  /**
   * Natural 3D padding
   */
  def padding3D(elemSize:Int,width:Long,height:Long,depth:Long): Long = 4


  /** 
   * Estimated available memory
   */
  def availableMemory: Long

  /**
   * Allocate a buffer
   */
  def allocate(size:Long): Buffer
}
