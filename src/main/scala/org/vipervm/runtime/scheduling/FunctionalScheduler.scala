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

package org.vipervm.runtime.scheduling

import org.vipervm.runtime.{FunctionalKernel,Data,Task}
import org.vipervm.platform.{Processor,MemoryNode,Platform}

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
  def submit(f:FunctionalKernel, input:List[Data]):List[Data] = {
    /* Create a task from the kernel and its parameters */
    val task = f.createTask(input)

    /* Put instance into run queue */
    submit(task)

    /* Return output data */
    task.output
  }

  /**
   * Discard some data
   *
   * Discarded data mustn't be used by functions submitted afterwards
   */
  def discard(ds:Data*): Unit

  /**
   * Submit a task to be executed
   */
  protected def submit(task:Task): Unit

  /**
   * Schedule a task on a given processor and memory
   * Required data must be present in memory
   */
  protected def schedule(task:Task,proc:Processor,memory:MemoryNode): Unit = {}

  /**
   * Platform 
   */
  val platform:Platform
}
