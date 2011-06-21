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

package org.vipervm.runtime

import org.vipervm.platform.Kernel

/**
 * Describe how to make a kernel pure
 *
 * A kernel can modify buffers passed as parameters. The runtime system
 * uses methods of this class to know if it has to duplicate data before
 * executing the kernel.
 * 
 * Order of kernel parameters is also defined as well as which data should
 * be considered as function output.
 *
 * @param kernel Kernel decorated by this class
 */
abstract class FunctionalKernel(val kernel:Kernel) {
  /** Number of arguments */
  val paramCount:Int

  /**
   * Prepare parameters for a kernel
   *  - Allocate data
   *  - Set order of kernel parameters
   */
  def pre(input:List[Data],output:Data):Seq[KernelParameter]

  /**
   * Set function's output from kernel parameters
   */
  def post(ks:Seq[KernelParameter],output:Data):Unit

  /**
   * Create output data
   */
  protected def createOutput(input:List[Data]):Data

  /**
   * Create a task from this kernel
   */
  def createTask(input:List[Data]) = new Task(this, input, createOutput(input))
}
