/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**                     GPLv3
*/

package fr.hsyl20.auratune.runtime

/**
 * A task scheduled on a device.
 */
class ScheduledTask(val task:Task, val kernel:Kernel, val device:Device, memoryNode:MemoryNode) {

  private val kernelParameters = task.args.map( _._1.toKernelParameter(memoryNode))

  val configuredKernel = ConfiguredKernel(kernel, kernelParameters)

  /**
   * Execute this task
   */
  def execute: RunningKernel = {
    device.execute(configuredKernel)
  }

  /**
   * Can be executed?
   */
  def canExecute: Boolean = {
    device.canExecute(configuredKernel)
  }
}
