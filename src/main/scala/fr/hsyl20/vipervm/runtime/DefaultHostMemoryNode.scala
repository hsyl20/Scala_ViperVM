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

package fr.hsyl20.vipervm.runtime

import com.sun.jna.Memory

/**
 * Default implementation for the host memory node
 *
 * Some specialized implementations could be provided.
 * For instance, NUMA architectures may have a different
 * host memory node. Or a different malloc could be used.
 */
class DefaultHostMemoryNode extends HostMemoryNode {

  type BufferType = DefaultHostBuffer
  
  /**
   * Allocate a buffer in host memory
   *
   * This uses default malloc implementation (used by JNA)
   *
   * @param size Size of the buffer (in bytes)
   */
  def allocate(size:Long) = new DefaultHostBuffer(size,this)

  def availableMemory:Long = {
    val bean = java.lang.management.ManagementFactory.getOperatingSystemMXBean
    val b = bean.asInstanceOf[com.sun.management.OperatingSystemMXBean]
    b.getFreePhysicalMemorySize
  }
}
