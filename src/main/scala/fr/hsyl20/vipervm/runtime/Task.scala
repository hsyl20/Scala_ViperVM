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

import fr.hsyl20.vipervm.runtime.AccessMode._

/*/**
 * A task ready to be scheduled.
 */
case class Task(kernels:KernelSet, args:List[(TaskParameter,AccessMode)]) {
  
  /**
   * Return Data parameters
   */
  def data: List[(Data,AccessMode)] = args.flatMap(a => a._1 match {
    case DataTaskParameter(d) => Some((d,a._2))
    case _ => None
  })
}*/
