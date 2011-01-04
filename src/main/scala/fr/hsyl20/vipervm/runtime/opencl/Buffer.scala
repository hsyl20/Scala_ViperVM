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

  private val device = memoryNode.device

  private implicit def buf2buf(b:Buffer): OpenCLBuffer = b.asInstanceOf[OpenCLBuffer]

  def free(): Unit = peer.release

}
