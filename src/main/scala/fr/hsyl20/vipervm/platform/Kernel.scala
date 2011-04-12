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
  def getParams(args:Seq[KernelParameter]): Option[ParamsType]

  /**
   * Test if this kernel can be executed by the given processor
   */
  def canExecuteOn(proc:Processor): Boolean

}

