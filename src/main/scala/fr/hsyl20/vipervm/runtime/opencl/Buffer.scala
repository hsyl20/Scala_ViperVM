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

package fr.hsyl20.vipervm.runtime.opencl

import fr.hsyl20.vipervm.runtime.Buffer
import fr.hsyl20.{opencl => cl}
import com.sun.jna.Memory

/**
 * OpenCL buffer
 */
class OpenCLBuffer(val peer: cl.Buffer, val memoryNode:OpenCLMemoryNode) extends Buffer {

  val device = memoryNode.device

  implicit def buf2buf(b:Buffer): OpenCLBuffer = b.asInstanceOf[OpenCLBuffer]

  def free: Unit = peer.release

  /**
   * Copy data from host to buffer
   */
  protected def driverCopyFromHost(src:Memory,size:Long,srcOffset:Long,dstOffset:Long): OpenCLEvent = {
    val ptr = src.getPointer(srcOffset)
    val clEvent = device.commandQueue.enqueueWriteBuffer(peer, false, dstOffset, size, ptr, Nil)
    new OpenCLEvent(clEvent)
  }

  /**
   * Copy data from buffer to host
   */
  protected def driverCopyToHost(dst:Memory,size:Long,srcOffset:Long,dstOffset:Long): OpenCLEvent = {
    val ptr = dst.getPointer(dstOffset)
    val clEvent = device.commandQueue.enqueueReadBuffer(peer, false, srcOffset, size, ptr, Nil)
    new OpenCLEvent(clEvent)
  }
}
