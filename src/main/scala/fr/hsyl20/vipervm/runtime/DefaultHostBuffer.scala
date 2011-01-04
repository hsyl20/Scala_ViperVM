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
 * Default host buffer implementation
 */
class DefaultHostBuffer(val peer:Memory, val memoryNode:HostMemoryNode) extends HostBuffer {

  def this(size:Long,memoryNode:HostMemoryNode) = this(new Memory(size), memoryNode)

  /**
   * Free the buffer in memory node
   *
   * Current implementation free host buffer on GC.
   * TODO: force instant call of free
   */
  def free():Unit = {}
}
