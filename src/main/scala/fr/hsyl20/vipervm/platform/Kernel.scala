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

package fr.hsyl20.vipervm.platform

/**
 * A kernel 
 *
 * Backends provide concrete implementations
 */
trait Kernel {

  type ParamsType

  /**
   * Check that parameters are valid and return them in a convenient object
   */
  val extractParameters: PartialFunction[Seq[KernelParameter],ParamsType]

  /**
   * Test if this kernel can be executed by the given processor
   */
  def canExecuteOn(proc:Processor): Boolean

  /**
   * Access modes for kernel parameters
   */
  val modes: Seq[AccessMode]

}

sealed abstract class AccessMode
case object ReadOnly extends AccessMode
case object ReadWrite extends AccessMode
