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

/**
 * A runtime system
 *
 * A runtime system is made of
 *  - a platform
 *  - a task scheduler
 *  - a data scheduler
 */
abstract class Runtime {
  val platform:Platform
  //val taskScheduler:Scheduler

  /** Memory node for the host */
  val hostMemoryNode:HostMemoryNode = new DefaultHostMemoryNode

  /**
   * Allocate a data somewhere
   *
   * Default behavior is to allocate in host memory node
   */
/*  def allocate(data:Data):Unit = {
    data.status(hostMemoryNode) match {
      case None =>
        val size = data.sizeOn(hostMemoryNode)
        val buf = hostMemoryNode.allocate(size)
        data.addBuffer(buf)
      case _ => ()
    }
  }*/
}
