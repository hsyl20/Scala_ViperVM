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

package fr.hsyl20.vipervm.platform

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
   * Execute the kernel with the specified parameters
   */
  def execute(kernel:Kernel, args:Seq[KernelParameter]): KernelEvent
}
