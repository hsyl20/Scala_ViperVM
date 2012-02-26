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

import org.vipervm.platform.{Kernel,Processor}

/**
 * A kernel set is a set of kernels accomplishing the same task and
 * that can be run on different devices
 *
 */
trait KernelSet extends MetaKernel {

  val kernels:Seq[Kernel]

  //TODO: check that prototypes of all kernels are the same

  /**
   * Return kernels that can be executed on the processor
   */
  def getKernelsFor(proc:Processor): Seq[Kernel] = kernels.filter(_.canExecuteOn(proc))

}
