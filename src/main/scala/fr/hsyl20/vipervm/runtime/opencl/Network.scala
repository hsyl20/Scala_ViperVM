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

import fr.hsyl20.vipervm.runtime._

/**
 * Network between OpenCL device and host memory
 */
class OpenCLNetwork(val device:OpenCLDevice) extends Network {

  private val mem = device.memory

  /**
   * Return a link from source to target using this network if possible
   */
  def link(source:MemoryNode,target:MemoryNode): Option[Link] = (source,target) match {
    case (`mem`,n:HostMemoryNode) => Some(OpenCLSourceLink(this, mem, n))
    case (n:HostMemoryNode,`mem`) => Some(OpenCLTargetLink(this, n, mem))
    case _ => None
  }

  def copy(link:Link,source:Buffer,target:Buffer,size:Long,sourceOffset:Long=0,targetOffset:Long=0):DataTransfer1D = {

    link match {
      case OpenCLSourceLink(net,src,tgt) => {
        val cq = net.device.commandQueue
        val srcPeer = src.get(source).peer
        val tgtPeer = tgt.get(target).peer
        val ptr = tgtPeer.getPointer(targetOffset)
        val ev = cq.enqueueReadBuffer(srcPeer, false, sourceOffset, size, ptr, Nil)
        val event = new OpenCLEvent(ev)
        DataTransfer1D(link,source,target,size,sourceOffset,targetOffset,event)
      }
      case OpenCLTargetLink(net,src,tgt) => {
        val cq = net.device.commandQueue
        val srcPeer = src.get(source).peer
        val tgtPeer = tgt.get(target).peer
        val ptr = srcPeer.getPointer(sourceOffset)
        val ev = cq.enqueueWriteBuffer(tgtPeer, false, targetOffset, size, ptr, Nil)
        val event = new OpenCLEvent(ev)
        DataTransfer1D(link,source,target,size,sourceOffset,targetOffset,event)
      }
      case _ => throw new Exception("trying to copy with invalid link")
    }
  }

}


