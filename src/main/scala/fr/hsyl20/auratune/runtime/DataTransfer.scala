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

case class DataTransfer1D(direction:DataTransferDirection,
  offset:Long, memory:Memory, memoryOffset:Long, event:Event,
  size:Long) extends DataTransfer

case class DataTransfer2D(direction:DataTransferDirection,
  offset:Long, memory:Memory, memoryOffset:Long, event:Event,
  elemSize:Int, width:Long, height:Long,
  padding:Long, memoryPadding:Long) extends DataTransfer

case class DataTransfer3D(direction:DataTransferDirection,
  offset:Long, memory:Memory, memoryOffset:Long, event:Event,
  elemSize:Int, width:Long, height:Long, depth:Long,
  rowPadding:Long, planePadding:Long,
  memoryRowPadding:Long, memoryPlanePadding:Long) extends DataTransfer

/**
 * Event associated to a data transfer
 */
abstract class DataTransfer {
  val direction:DataTransferDirection
  val offset:Long
  val memory:Memory
  val memoryOffset:Long
  val event:Event
}


sealed abstract class DataTransferDirection
case object HostToMemoryNode extends DataTransferDirection
case object MemoryNodeToHost extends DataTransferDirection
