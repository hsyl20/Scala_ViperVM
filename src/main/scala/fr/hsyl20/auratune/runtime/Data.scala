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
import scala.collection._

/** This represents a data. That is, a set of buffers
 * in different memories
 */
abstract class Data {
  import Data._

  /* Buffers on different memory nodes */
  protected var buffers: mutable.Map[MemoryNode,Buffer] = mutable.Map.empty

  /* Buffer status */
  protected var status: mutable.Map[Buffer,State] = mutable.Map.empty

  /**
   * Return the buffer associated to this data on the given memory node
   */
  def getBuffer(memoryNode:MemoryNode): Option[Buffer] = buffers.get(memoryNode)

  /**
   * Return the status of this data on the given node
   */
  def status(memoryNode:MemoryNode): Option[State] = getBuffer(memoryNode).flatMap(status.get _)


  /**
   * Allocate a buffer for this data on some memory node
   */
  def allocate(memoryNode:MemoryNode): Event

  /**
   * Required size of the buffer for this data on the given node
   */
  def sizeOn(memoryNode:MemoryNode): Long

  /**
   * Update the buffer on memoryNode if it is invalid
   */
  def sync(memoryNode:MemoryNode): Event

  /**
   * Add a buffer to this data on a different memory node.
   *
   * If a buffer for this data is already present on the memory node, an
   * exception is thrown
   */
  def addBuffer(buffer:Buffer): Unit = {
    getBuffer(buffer.memoryNode) match {
      case Some(_) => throw new Exception("A buffer for this data is already present on the same memory node")
      case None => buffers += (buffer.memoryNode -> buffer)
    }
  }

  /**
   * Remove a reference to a buffer supporting this data
   *
   * Return the removed buffer
   */
  def removeBuffer(buffer:Buffer): Option[Buffer] = buffers.remove(buffer.memoryNode)


  /**
   * This method is called before an access is to be made
   * on buffer associated to this data
   */
  def notifyBeforeAccess(buffer:Buffer, mode:AccessMode): Unit = {}

  /**
   * This method is called after an access is to be made
   * on buffer associated to this data
   */
  def notifyAfterAccess(buffer:Buffer, mode:AccessMode): Unit = {}
}



object Data {
  sealed abstract class State
  case object Ready extends State
  case class Shared(readerCount:Int) extends State /* Shared in Read-Only mode */
  case object Invalid extends State
  case object Exclusive extends State
}
