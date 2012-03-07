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

package org.vipervm.runtime.mm

import org.vipervm.platform.MemoryNode

/** Instance of a data */
trait DataInstance {

  /** Selected way to represent the data */
  val repr:Repr

  /**
   * Indicate whether a data is available in a memory
   *
   * If the data is a composition of other data, the latter are returned
   * otherwise a boolean is returned.
   */
  def isAvailableIn(memory:MemoryNode):Either[Seq[Data],Boolean]
}
