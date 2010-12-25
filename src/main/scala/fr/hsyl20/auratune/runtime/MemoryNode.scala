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
  def padding2D(elemSize:Int,width:Long,height:Long): Long = 0
  /**
   * Natural 3D padding
   */
  def padding3D(elemSize:Int,width:Long,height:Long,depth:Long): Long = 0


  /** 
   * Estimated available memory
   */
  def availableMemory: Long

  /**
   * Allocate a buffer
   */
  def allocate(size:Long): Buffer

  /**
   * Allocate with natural 2D padding
   *
   * Default behavior is no padding
   *
   * @return (Buffer,padding)
   */
  def allocate2D(elemSize:Int,width:Long,height:Long): (Buffer,Long) = {
    (allocate(elemSize*width*height), padding2D(elemSize,width,height))
  }

  /**
   * Allocate with natural 3D padding
   *
   * Default behavior is no padding
   *
   * @return (Buffer,padding1,padding2)
   */
  def allocate3D(elemSize:Int,width:Long,height:Long,depth:Long): (Buffer,Long,Long) = {
    val pad1 = padding2D(elemSize,width,height)
    val pad2 = padding3D(elemSize,width,height,depth)
    (allocate(elemSize*width*height*depth), pad1, pad2)
  }
}
