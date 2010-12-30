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

import com.sun.jna.Memory

import fr.hsyl20.vipervm.runtime.{MemoryNode,Buffer}
import fr.hsyl20.{opencl => cl}

/* OpenCL memory node */
class OpenCLMemoryNode(val device:OpenCLDevice) extends MemoryNode {

  val devices = List(device)

  implicit def buffer2buffer(b:Buffer): OpenCLBuffer = b.asInstanceOf[OpenCLBuffer]

  private var _availableMemory: Long = device.peer.globalMemSize

  def availableMemory: Long = _availableMemory

  /**
   * Allocate a OpenCL buffer
   */
  override def allocate(size:Long): OpenCLBuffer = {
    val peer = cl.Buffer.create(device.context, size)
    _availableMemory -= size
    new OpenCLBuffer(peer, this)
  }
}

