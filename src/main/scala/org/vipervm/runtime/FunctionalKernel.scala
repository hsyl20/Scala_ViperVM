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

import org.vipervm.platform.{Kernel,KernelParameter}

/**
 * A functional kernel
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
abstract class FunctionalKernel(val kernel:MetaKernel) {
  /** Number of arguments */
  val paramCount:Int

  /**
   * Prepare parameters for kernel execution
   *  - Transform views into buffer + immediate values
   */
  def pre(input:Seq[Value]):Seq[KernelParameter]

  /**
   * Select outputs from the whole kernel parameter list
   */
  def post(output:Seq[KernelParameter]):Value

  /**
   * Create kernel output parameters from inputs
   *  - Allocate data
   * @return input and output parameters
   */
  protected def createOutputs(args:Seq[Value]):Seq[Value]

}
