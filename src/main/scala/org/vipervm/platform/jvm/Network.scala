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

package org.vipervm.platform.jvm

import scala.actors.Futures._
import org.vipervm.platform._

class JVMNetwork extends Network {
  val memoryCopier = new JVMMemoryCopier

  def link(source:MemoryNode,target:MemoryNode): Option[Link] = (source,target) match {
    case (JVMMemoryNode,n:HostMemoryNode) => Some(JVMReadLink(this, n))
    case (n:HostMemoryNode,JVMMemoryNode) => Some(JVMWriteLink(this, n))
    case _ => None
  }
}

class JVMMemoryCopier extends MemoryCopier with Copy1DSupport {
  def copy1D(link:Link,source:BufferView1D,target:BufferView1D):DataTransfer = {
    link match {
      case JVMReadLink(net,tgtMem) => {
        val srcPeer = JVMMemoryNode.get(source.buffer).peer
        val tgtPeer = tgtMem.get(target.buffer).peer
        val event = new JVMEvent(
          future {
            tgtPeer.write(target.offset, srcPeer, source.offset.toInt, target.size.toInt)
          }
        )
        new DataTransfer(link,source,target,event)
      }
      case JVMWriteLink(net,srcMem) => {
        val srcPeer = srcMem.get(source.buffer).peer
        val tgtPeer = JVMMemoryNode.get(target.buffer).peer
        val event = new JVMEvent(
          future(srcPeer.read(source.offset, tgtPeer, target.offset.toInt, target.size.toInt))
        )
        new DataTransfer(link,source,target,event)
      }
      case _ => throw new Exception("trying to copy with invalid link")
    }
  }
}

