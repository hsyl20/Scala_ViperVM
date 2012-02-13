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

  protected var fviews: HashMap[MemoryNode,ViewType] = HashMap.empty

  /** Indicate whether this data has been computed */
  def isDefined:Boolean = !views.isEmpty

  def views:Map[MemoryNode,ViewType] = fviews.toMap

  def viewIn(memory:MemoryNode):Option[ViewType] = fviews.get(memory)

  def allocate(memory:MemoryNode):ViewType

  /** Store a view containing valid contents for this data */
  def store(memory:MemoryNode,view:ViewType):Unit = {
    fviews.synchronized {
      fviews.update(memory,view)
    }
  }

}
