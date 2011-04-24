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

package fr.hsyl20.vipervm.runtime.scheduling

import fr.hsyl20.vipervm.runtime.{FunctionalKernel,Data,FunctionalKernelInstance}

/**
 * Schedule functional kernels
 */
trait FunctionalScheduler {

  /**
   * Schedule the given functional kernel
   *
   * @param f The kernel to schedule
   * @param input Kernel inputs
   * @return kernel outputs
   */
  def schedule(f:FunctionalKernel, input:List[Data]):List[Data] = {
    /* Create kernel instance */
    val instance = f.createInstance(input)

    /* Put instance into run queue */
    enqueue(instance)

    /* Return output data */
    instance.output
  }

  /**
   * Enqueue an instance to be executed
   */
  protected def enqueue(instance:FunctionalKernelInstance): Unit
}
