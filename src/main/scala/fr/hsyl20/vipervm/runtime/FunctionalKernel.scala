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

import fr.hsyl20.vipervm.platform.Kernel

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
abstract class FunctionalKernel(kernel:Kernel) {
  /**
   * Prepare parameters for a kernel
   *  - Allocate data
   *  - Set order of kernel parameters
   */
  def pre(ks:Seq[KernelParameter]):Seq[KernelParameter]

  /**
   * Access modes for kernel parameters
   */
  val modes: Seq[AccessMode]

  /**
   * Get function's outputs from kernel parameters
   */
  def post(ks:Seq[KernelParameter]):Seq[KernelParameter]
}


