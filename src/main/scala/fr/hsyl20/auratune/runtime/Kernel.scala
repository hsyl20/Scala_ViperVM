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
 * Abstract class for kernels.
 *
 * Backends provide concrete implementations
 */
abstract class Kernel {
  /**
   * Execute the kernel with the specified parameters
   */
  def execute(device:Device, args:Seq[KernelParameter]): Event

  /**
   * Test if the kernel can be executed with the given parameters
   * Return a list of errors or Nil if none
   */
  def canExecute(device:Device, args:Seq[KernelParameter]): Boolean
}
