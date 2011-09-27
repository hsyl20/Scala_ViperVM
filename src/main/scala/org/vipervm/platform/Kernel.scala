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

package org.vipervm.platform

/**
 * A kernel 
 *
 * Backends provide concrete implementations
 */
trait Kernel {

  /**
   * Test if this kernel can be executed by the given processor
   */
  def canExecuteOn(proc:Processor): Boolean

}

sealed abstract class AccessMode
case object ReadOnly extends AccessMode
case object ReadWrite extends AccessMode

case class Param[A<:KernelParameter](position:Int,mode:AccessMode) {
  def apply(s:Seq[KernelParameter]):A = s.drop(position).head.asInstanceOf[A]
}
