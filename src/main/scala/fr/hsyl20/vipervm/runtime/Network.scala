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
 * A network between different memory nodes
 */
abstract class Network {
  /**
   * Return a link from source to target using this network if possible
   */
  def link(source:MemoryNode,target:MemoryNode): Option[Link]

  /**
   * Schedule an asynchronous copy of data
   *
   * @param link    Link handling the transfer
   * @param source  Source buffer
   * @param target  Target buffer
   * @param size    Size to copy (in bytes)
   * @param sourceOffset Offset in source buffer (in bytes)
   * @param targetOffset Offset in target buffer (in bytes)
   */
  def copy(link:Link,source:Buffer,target:Buffer,size:Long,sourceOffset:Long=0,targetOffset:Long=0):DataTransfer1D
}

/**
 * Link support for 2D copy
 */
trait Copy2DSupport {
  /**
   * Schedule an asynchronous copy of 2D data
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
  def copy2D(link:Link,source:Buffer,target:Buffer,
    elemSize:Long,width:Long,height:Long,
    sourcePadding:Long,targetPadding:Long,
    sourceOffset:Long=0,targetOffset:Long=0):DataTransfer2D
}

/**
 * Link support for 3D copy
 */
trait Copy3DSupport {
  /**
   * Schedule an asynchronous copy of 3D data
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
  def copy3D(link:Link,source:Buffer,target:Buffer,
    elemSize:Long,width:Long,height:Long,
    sourceRowPadding:Long,targetRowPadding:Long,
    sourcePlanePadding:Long,targetPlanePadding:Long,
    sourceOffset:Long=0,targetOffset:Long=0):DataTransfer3D
}

