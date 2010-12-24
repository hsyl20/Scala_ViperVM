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

package fr.hsyl20.auratune.runtime.opencl

import com.sun.jna.Memory

import fr.hsyl20.auratune.runtime.{MemoryNode,Buffer}
import fr.hsyl20.{opencl => cl}

/* OpenCL memory node */
class OpenCLMemoryNode(val device:OpenCLDevice) extends MemoryNode {

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

