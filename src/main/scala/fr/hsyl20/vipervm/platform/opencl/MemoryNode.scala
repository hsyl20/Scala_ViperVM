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

package fr.hsyl20.vipervm.platform.opencl

import com.sun.jna.Memory

import fr.hsyl20.vipervm.platform.{MemoryNode,Buffer}
import fr.hsyl20.{opencl => cl}

/* OpenCL memory node */
class OpenCLMemoryNode(val device:OpenCLProcessor) extends MemoryNode {

  type BufferType = OpenCLBuffer

  implicit def buffer2buffer(b:Buffer): OpenCLBuffer = b.asInstanceOf[OpenCLBuffer]

  def availableMemory: Long = device.peer.globalMemSize - buffers.map(_.size).sum

  /**
   * Allocate a OpenCL buffer on this memory node
   *
   * @param size Size of the buffer
   */
  override def allocate(size:Long): OpenCLBuffer = {
    val peer = cl.Buffer.create(device.context, size)
    val b = new OpenCLBuffer(size, peer, this)
    buffers += b
    b
  }

  /**
   * Free a buffer from this memory node
   */
  def free(buffer:OpenCLBuffer): Unit = {
    if (!buffers.contains(buffer))
      throw new Exception("Trying to remoe a buffer from a memory that does not contain it")

    buffers -= buffer
    buffer.peer.release
  }

  override def equals(a:Any):Boolean = a match {
    case a:OpenCLMemoryNode if a.device == device => true
    case _ => false
  }
}

