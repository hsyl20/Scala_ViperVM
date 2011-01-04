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
 * Data transfer for contiguous memory
 *
 * @param link    Link handling the transfer
 * @param source  Source buffer
 * @param target  Target buffer
 * @param size    Size to copy (in bytes)
 * @param sourceOffset Offset in source buffer (in bytes)
 * @param targetOffset Offset in target buffer (in bytes)
 */
case class DataTransfer1D(link:Link,source:Buffer,target:Buffer,size:Long,
  sourceOffset:Long,targetOffset:Long,
  event:Event) extends DataTransfer

/**
 * 2D data transfer
 *
 * @param link    Link handling the transfer
 * @param source  Source buffer
 * @param target  Target buffer
 * @param elemSize Size of one element (in bytes)
 * @param width   Row width
 * @param height  Row count
 * @param sourcePadding Row padding in source buffer (in bytes)
 * @param targetPadding Row padding in target buffer (in bytes)
 * @param sourceOffset Offset in source buffer (in bytes)
 * @param targetOffset Offset in target buffer (in bytes)
 */
case class DataTransfer2D(link:Link,source:Buffer,target:Buffer,
  elemSize:Long,width:Long,height:Long,
  sourcePadding:Long,targetPadding:Long,
  sourceOffset:Long,targetOffset:Long,
  event:Event) extends DataTransfer

/**
 * 3D data transfer
 *
 * @param link    Link handling the transfer
 * @param source  Source buffer
 * @param target  Target buffer
 * @param elemSize Size of one element (in bytes)
 * @param width   Row width
 * @param height  Row count
 * @param depth   Plane count
 * @param sourceRowPadding Row padding in source buffer (in bytes)
 * @param targetRowPadding Row padding in target buffer (in bytes)
 * @param sourcePlanePadding Row padding in source buffer (in bytes)
 * @param targetPlanePadding Row padding in target buffer (in bytes)
 * @param sourceOffset Offset in source buffer (in bytes)
 * @param targetOffset Offset in target buffer (in bytes)
 */
case class DataTransfer3D(link:Link,source:Buffer,target:Buffer,
  elemSize:Long,width:Long,height:Long,
  sourceRowPadding:Long,targetRowPadding:Long,
  sourcePlanePadding:Long,targetPlanePadding:Long,
  sourceOffset:Long,targetOffset:Long,
  event:Event) extends DataTransfer

/**
 * Data transfer
 */
abstract class DataTransfer {
  /** Link handling the data transfer */
  val link:Link
  /** Source of the data transfer */
  val source:Buffer
  /** Destination of the data transfer */
  val target:Buffer
  /** Offset in the source buffer */
  val sourceOffset:Long
  /** Offset in the destination buffer */
  val targetOffset:Long
  /** Event indicating data transfer completion  */
  val event:Event
}
