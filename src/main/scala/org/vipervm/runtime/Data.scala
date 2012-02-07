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

sealed abstract class DataState
case object InvalidData extends DataState
case object ValidData   extends DataState

abstract class Data {
  type ViewType <: BufferView

  protected var views: HashMap[MemoryNode,(ViewType,DataState)] = HashMap.empty

  /** Indicate whether this data has been computed */
  def isDefined:Boolean = !views.isEmpty

  def viewIn(memory:MemoryNode):Option[ViewType] = views.get(memory).map(_._1)

  def stateIn(memory:MemoryNode):Option[DataState] = views.get(memory).map(_._2)

  def allocate(memory:MemoryNode):ViewType

  /**
   * Allocate a view and store it in the ViewSet
   */
  def allocateStore(memory:MemoryNode):ViewType = {
    val view = allocate(memory)
    store(memory,view,InvalidData)
    view
  }

  def store(memory:MemoryNode,view:ViewType,state:DataState = InvalidData):Unit = {
    views.synchronized {
      views.update(memory,(view,state))
    }
  }

}
