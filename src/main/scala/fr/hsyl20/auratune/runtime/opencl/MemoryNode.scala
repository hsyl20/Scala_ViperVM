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
class OpenCLMemoryNode(device:OpenCLDevice) extends MemoryNode {

  implicit def buffer2buffer(b:Buffer): OpenCLBuffer = b.asInstanceOf[OpenCLBuffer]

  /**
   * Allocate a OpenCL buffer
   */
  override def allocate(size:Long): OpenCLBuffer = {
    val peer = cl.Buffer.create(device.context, size)
    new OpenCLBuffer(peer, this)
  }

  /**
   * Copy data from host to buffer
   */
  def copyFromHost(src:Memory,dst:Buffer,size:Long,srcOffset:Long=0,dstOffset:Long=0): OpenCLEvent = {
    val ptr = src.getPointer(srcOffset)
    val clEvent = device.commandQueue.enqueueWriteBuffer(dst.peer, false, dstOffset, size, ptr, Nil)
    new OpenCLEvent(clEvent)
  }

  /**
   * Copy data from buffer to host
   */
  def copyToHost(src:Buffer,dst:Memory,size:Long,srcOffset:Long=0,dstOffset:Long=0): OpenCLEvent = {
    val ptr = dst.getPointer(dstOffset)
    val clEvent = device.commandQueue.enqueueReadBuffer(src.peer, false, srcOffset, size, ptr, Nil)
    new OpenCLEvent(clEvent)
  }
}

