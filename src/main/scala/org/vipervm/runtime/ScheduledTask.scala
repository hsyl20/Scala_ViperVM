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

package org.vipervm.runtime

/*/**
 * A task scheduled on a device.
 */
class ScheduledTask(val task:Task, val kernel:Kernel, val proc:Processor, memoryNode:MemoryNode) {

  private val kernelParameters = task.args.map( _._1.toKernelParameter(memoryNode))

  val configuredKernel = ConfiguredKernel(kernel, kernelParameters)

  /**
   * Execute this task
   */
  def execute: RunningKernel = {
    proc.execute(configuredKernel)
  }

  /**
   * Can be executed?
   */
  def canExecute: Boolean = {
    proc.canExecute(configuredKernel)
  }
}*/
