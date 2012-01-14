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

package org.vipervm.platform.opencl

import org.vipervm.platform._

import com.sun.jna.Pointer

/**
 * Network between OpenCL device memory and host memory
 */
class OpenCLNetwork(val device:OpenCLProcessor) extends Network {

  private val mem = device.memory

  val memoryCopier = new OpenCLMemoryCopier

  /**
   * Return a link from source to target using this network if possible
   */
  def link(source:MemoryNode,target:MemoryNode): Option[Link] = (source,target) match {
    case (m:OpenCLMemoryNode,n:HostMemoryNode) if m == mem => Some(OpenCLReadLink(this, mem, n))
    case (n:HostMemoryNode,m:OpenCLMemoryNode) if m == mem => Some(OpenCLWriteLink(this, n, mem))
    case _ => None
  }

}

class OpenCLMemoryCopier extends MemoryCopier with Copy1DSupport {
  def copy1D(link:Link,source:BufferView1D,target:BufferView1D):DataTransfer = {
    link match {
      case OpenCLReadLink(net,srcMem,tgtMem) => {
        val cq = net.device.commandQueue
        val srcPeer = srcMem.get(source.buffer).peer
        val tgtPeer = tgtMem.get(target.buffer).peer
        val ptr = new Pointer(Pointer.nativeValue(tgtPeer) + target.offset)
        val ev = cq.enqueueReadBuffer(srcPeer, false, source.offset, source.size, ptr, Nil)
        val event = new OpenCLEvent(ev)
        new DataTransfer(link,source,target,event)
      }
      case OpenCLWriteLink(net,srcMem,tgtMem) => {
        val cq = net.device.commandQueue
        val srcPeer = srcMem.get(source.buffer).peer
        val tgtPeer = tgtMem.get(target.buffer).peer
        val ptr = new Pointer(Pointer.nativeValue(srcPeer) + source.offset)
        val ev = cq.enqueueWriteBuffer(tgtPeer, false, target.offset, source.size, ptr, Nil)
        val event = new OpenCLEvent(ev)
        new DataTransfer(link,source,target,event)
      }
      case _ => throw new Exception("trying to copy with invalid link")
    }
  }
}

