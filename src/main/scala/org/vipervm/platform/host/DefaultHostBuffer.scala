/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.platform.host

import org.vipervm.platform.{HostBuffer,HostMemoryNode}
import com.sun.jna.Memory

/**
 * Default host buffer implementation
 */
class DefaultHostBuffer(val peer:Memory, val memory:HostMemoryNode) extends HostBuffer {

  def this(size:Long,memory:HostMemoryNode) = this(new Memory(size), memory)

  val size = peer.size

  /**
   * Free the buffer in memory node
   *
   * Current implementation free host buffer on GC.
   * TODO: force instant call of free
   */
  def free():Unit = {}
}
