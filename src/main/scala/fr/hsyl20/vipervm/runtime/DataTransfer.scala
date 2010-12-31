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

import com.sun.jna.Memory

/**
 * Data transfer for contiguous memory
 *
 * @param size Size of data to transfer in bytes
 */
case class DataTransfer1D(direction:DataTransferDirection,
  offset:Long, memory:Memory, memoryOffset:Long, event:Event,
  size:Long) extends DataTransfer

/**
 * 2D data transfer
 *
 * @param elemSize Size of an element (in bytes)
 * @param width Number of elements in one row
 * @param height Number of rows
 * @param padding Padding bytes for each row (on memory node)
 * @param memoryPadding Padding bytes for each row (on host)
 */
case class DataTransfer2D(direction:DataTransferDirection,
  offset:Long, memory:Memory, memoryOffset:Long, event:Event,
  elemSize:Int, width:Long, height:Long,
  padding:Long, memoryPadding:Long) extends DataTransfer

/**
 * 3D data transfer
 *
 * @param elemSize Size of an element (in bytes)
 * @param width Number of elements in one row
 * @param height Number of rows in a plane
 * @param depth Number of planes
 * @param rowPadding Padding bytes for each row (on memory node)
 * @param planePadding Padding bytes for each plane (on memory node)
 * @param memoryRowPadding Padding bytes for each row (on host)
 * @param memoryPlanePadding Padding bytes for each plane (on host)
 */
case class DataTransfer3D(direction:DataTransferDirection,
  offset:Long, memory:Memory, memoryOffset:Long, event:Event,
  elemSize:Int, width:Long, height:Long, depth:Long,
  rowPadding:Long, planePadding:Long,
  memoryRowPadding:Long, memoryPlanePadding:Long) extends DataTransfer

/**
 * Event associated to a data transfer
 */
abstract class DataTransfer {
  /** Direction (Host to memory node or vice-versa) */
  val direction:DataTransferDirection
  /** Offset in memory node buffer */
  val offset:Long
  /** Host data */
  val memory:Memory
  /** Offset for host data */
  val memoryOffset:Long
  /** Event indicating data transfer completion  */
  val event:Event
}


/** Direction of a data transfer  */
sealed abstract class DataTransferDirection
/** Direction: from host to memory node  */
case object HostToMemoryNode extends DataTransferDirection
/** Direction: from memory node to host */
case object MemoryNodeToHost extends DataTransferDirection
