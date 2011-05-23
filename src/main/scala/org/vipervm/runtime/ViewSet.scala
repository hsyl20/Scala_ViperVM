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

package org.vipervm.runtime

import org.vipervm.platform.BufferView
import org.vipervm.platform.MemoryNode

import scala.collection.mutable.HashMap

/**
 * A set of buffer views
 */
class ViewSet[V <: BufferView] extends HashMap[MemoryNode,V]

object ViewSet {
  def empty[V <: BufferView] = new ViewSet[V]
}
