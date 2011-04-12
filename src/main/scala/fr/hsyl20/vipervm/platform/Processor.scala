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
  /**
   * Memories in which the processor can work
   */
  def memories:Seq[MemoryNode]

  /**
   * Execute the kernel with the specified parameters
   */
  def execute(kernel:Kernel, args:Seq[KernelParameter]): KernelEvent
}
