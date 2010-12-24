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
