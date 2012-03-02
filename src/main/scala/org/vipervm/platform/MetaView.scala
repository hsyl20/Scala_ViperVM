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

import scala.collection.mutable.HashMap
import scala.collection.breakOut

abstract class MetaView {
  type ViewType <: BufferView

  protected var fviews: HashMap[MemoryNode,ViewType] = HashMap.empty

  /** Indicate whether this data has been computed */
  def isDefined:Boolean = fviews.synchronized {
    !fviews.isEmpty
  }

  def views:Seq[ViewType] = fviews.synchronized {
    fviews.values.map(identity)(breakOut)
  }

  def viewIn(memory:MemoryNode):Option[ViewType] = fviews.get(memory)

  def allocate(memory:MemoryNode):ViewType

  /** Store a view containing valid contents for this data */
  def store(view:ViewType):Unit = {
    val memory = view.buffer.memory
    fviews.synchronized {
      fviews.update(memory,view)
    }
  }

}
