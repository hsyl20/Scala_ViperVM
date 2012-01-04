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

package org.vipervm.platform

/**
 * A processor can execute programs to transform data that are stored in some memories
 */
abstract class Processor {
  type MemoryNodeType <: MemoryNode

  /**
   * Memories in which the processor can work
   */
  val memories:Seq[MemoryNodeType]

  /**
   * Default memory for this processor 
   */
  lazy val memory:MemoryNodeType = memories.head

  /**
   * Compile the kernel for this processor
   */
  def compile(kernel:Kernel):Unit

  /**
   * Execute the kernel with the specified parameters
   */
  def execute(kernel:Kernel, args:Seq[KernelParameter]): KernelEvent
}
