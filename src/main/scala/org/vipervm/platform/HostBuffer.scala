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

package org.vipervm.platform

import com.sun.jna.Memory
import java.nio.ByteBuffer

/**
 * A buffer in host memory
 */
abstract class HostBuffer extends Buffer {
  /** ByteBuffer mapping this buffer in memory */
  lazy val byteBuffer:ByteBuffer = peer.getByteBuffer(0, peer.size)

  /** Peer JNA Memory */
  val peer:Memory

  /** Associated memory node */
  val memory:HostMemoryNode
}
