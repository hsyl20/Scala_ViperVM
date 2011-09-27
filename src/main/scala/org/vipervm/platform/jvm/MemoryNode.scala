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

import org.vipervm.platform._

object JVMMemoryNode extends MemoryNode {
  type BufferType = JVMBuffer

  def availableMemory: Long = 10000000 //TODO

  override def allocate(size:Long): JVMBuffer = {
    if (size > Long.MaxValue)
      throw new Exception("Trying to allocate with size too big")

    val buf = new JVMBuffer(new Array[Byte](size.toInt))
    buffers += buf
    buf
  }


  def free(buffer:JVMBuffer): Unit = {
    buffers -= buffer
  }
}
