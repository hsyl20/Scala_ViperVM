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
class DataInstance(val typ:VVMType, val meta:MetaData, val repr:Repr, val properties:ReprProperties, val storage:Storage) {

  /**
   * Indicate whether a data is available in a memory
   */
  def isAvailableIn(memory:MemoryNode):Boolean = {
    storage.views.forall(_.buffer.memory == memory)
  }
}
