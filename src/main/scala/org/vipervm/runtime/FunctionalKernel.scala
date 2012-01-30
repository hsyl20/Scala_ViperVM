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
 */
trait FunctionalKernel {

  val peer:Kernel

  def createTask(args:Seq[TaskParameter]): (Task,Data)

}
