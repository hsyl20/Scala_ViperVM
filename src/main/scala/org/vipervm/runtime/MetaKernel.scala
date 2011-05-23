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

import org.vipervm.platform.{Kernel,Processor}

/**
 * A kernel that can be executed on different architectures
 *
 * Kernels must all take the same parameters as input (same prototype)
 */
trait MetaKernel {

  /**
   * Get the kernel for the given processor
   */
  def getKernelsFor(proc:Processor): Seq[Kernel]

}
