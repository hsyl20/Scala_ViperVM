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
 * A kernel set is a set of kernels accomplishing the same task and
 * that can be run on different devices
 *
 * Added kernels must support kernel set prototype (i.e. kernel parameters)
 * @param prototype Prototype for every kernel in this set
 */
class KernelSet(kernels:Seq[Kernel]) extends MetaKernel {

  //TODO: check that prototypes of all kernels are the same

  /**
   * Return kernels that can be executed on the processor
   */
  def getKernelsFor(proc:Processor): Seq[Kernel] = kernels.filter(_.canExecuteOn(proc))

}
