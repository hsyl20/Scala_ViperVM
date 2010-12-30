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

/** Device represents a GPU or a CPU+memory
 *
 */
abstract class Device {
  val memoryNodes: Seq[MemoryNode]

  /**
   * Execute the kernel with the specified parameters
   */
  def execute(kernel:ConfiguredKernel): RunningKernel

  /**
   * Test if the kernel can be executed with the given parameters
   * Return a list of errors or Nil if none
   */
  def canExecute(kernel:ConfiguredKernel): Boolean
}
