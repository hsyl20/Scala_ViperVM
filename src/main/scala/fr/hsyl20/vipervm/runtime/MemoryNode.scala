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

import scala.collection.immutable.{NumericRange,HashSet}

/**
 * A memory node
 */
abstract class MemoryNode {

  type BufferType <: Buffer

  protected var buffers:HashSet[Buffer] = HashSet.empty

  /** 
   * Estimated available memory
   */
  def availableMemory: Long

  /**
   * Allocate a buffer
   */
  def allocate(size:Long): BufferType

  /**
   * Returns the buffer with its real type if this memory node contains it.
   * Otherwise, an exception is thrown.
   */
  def get(buffer:Buffer): BufferType = {
    if (!buffers.contains(buffer))
      throw new Exception("This buffer doesn't belong to this memory node")
    buffer.asInstanceOf[BufferType]
  }
}
