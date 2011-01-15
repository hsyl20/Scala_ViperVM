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

import fr.hsyl20.vipervm.runtime.AccessMode._
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
  def getBuffer(memory:MemoryNode): Option[Buffer] = buffers.get(memory)

  /**
   * Return the status of this data on the given node
   */
  def status(memory:MemoryNode): Option[State] = getBuffer(memory).flatMap(status.get _)


  /**
   * Allocate a buffer for this data on some memory node
   *
   * Return true on success, false otherwise
   */
  def allocate(memory:MemoryNode): Boolean

  /**
   * Required size of the buffer for this data on the given node
   */
  def sizeOn(memory:MemoryNode): Long

  /**
   * List memory nodes that can be used as sources to update
   * buffer contained in given memoryNode
   */
  def syncSources(memory:MemoryNode): Seq[MemoryNode] =
    buffers.keys.filter(status(_) match {
      case Some(Ready) 
        |  Some(Shared(_)) => true
      case _ => false
    }).toSeq

  /**
   * Add a buffer to this data on a different memory node.
   *
   * If a buffer for this data is already present on the memory node, an
   * exception is thrown
   */
  def addBuffer(buffer:Buffer): Unit = {
    getBuffer(buffer.memory) match {
      case Some(_) => throw new Exception("A buffer for this data is already present on the same memory node")
      case None => buffers += (buffer.memory -> buffer)
    }
  }

  /**
   * Remove a reference to a buffer supporting this data
   *
   * Return the removed buffer
   */
  def removeBuffer(buffer:Buffer): Option[Buffer] = buffers.remove(buffer.memory)


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
