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

import org.vipervm.platform.{BufferView,MemoryNode}
import scala.collection.mutable.HashMap

abstract class Data {
  type ViewType <: BufferView

  var views: HashMap[MemoryNode,ViewType] = HashMap.empty

  /** Indicate whether this data has been computed */
  def isDefined:Boolean = !views.isEmpty

  /** Event triggered when this data is computed */
  val computedEvent:ComputedDataEvent = new ComputedDataEvent(this)

  def viewIn(memory:MemoryNode):Option[ViewType] = views.get(memory)

  def allocate(memory:MemoryNode):ViewType

  /**
   * Allocate a view and store it in the ViewSet
   */
  def allocateStore(memory:MemoryNode):ViewType = {
    val view = allocate(memory)
    views.synchronized {
      views.update(memory,view)
    }
    view
  }
}
