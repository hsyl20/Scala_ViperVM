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

abstract class Data {
  type ViewType <: BufferView

  var views = ViewSet.empty[ViewType]

  /** Indicate whether this data has been computed */
  def computed:Boolean = !views.isEmpty

  /** Event triggered when this data is computed */
  val computedEvent:ComputedDataEvent = new ComputedDataEvent(this)

  def viewIn(memory:MemoryNode):Option[ViewType] = views.get(memory)

  def allocate(memory:MemoryNode):ViewType
}
