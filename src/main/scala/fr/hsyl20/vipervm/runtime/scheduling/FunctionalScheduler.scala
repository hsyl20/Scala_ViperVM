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

import fr.hsyl20.vipervm.runtime.{FunctionalKernel,Data,Task}

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
    /* Create a task from the kernel and its parameters */
    val task = f.createTask(input)

    /* Put instance into run queue */
    submit(task)

    /* Return output data */
    task.output
  }

  /**
   * Submit a task to be executed
   */
  protected def submit(task:Task): Unit
}
