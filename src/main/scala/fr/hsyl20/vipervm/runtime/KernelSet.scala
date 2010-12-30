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

/**
 * A kernel set is a set of kernels accomplishing the same task and
 * that can be run on different devices
 *
 * Added kernels must support kernel set prototype (i.e. kernel parameters)
 * @param prototype Prototype for every kernel in this set
 */
abstract class KernelSet(prototype:KernelParameter*) {

  var kernels: List[Kernel] = Nil

  /**
   * Add a kernel to this set
   */
  def +=(k:Kernel): Unit = kernels ::= k

  def flatMap[B](f:Kernel=>Traversable[B]) = kernels.flatMap(f)

  def foreach[U](f:Kernel=>U) = kernels.foreach(f)

  def map[B](f:Kernel=>B) = kernels.map(f)

  def withFilter(p: Kernel=>Boolean) = kernels.withFilter(p)
}
