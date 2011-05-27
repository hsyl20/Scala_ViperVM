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

import org.vipervm.platform.{BufferView,Buffer}

/**
 * Manage active views
 *
 * - Signal buffers that have no active views
 * - Indicate views on data that aren't shared and can be modified inplace (TODO)
 */
abstract class ViewManager {

  private var views:Map[Buffer,List[BufferView]] = Map.empty

  /**
   * Register a view
   */
  def register(view:BufferView): Unit = {
    val bs = views.getOrElse(view.buffer, Nil)
    views = views.updated(view.buffer, view::bs)
  }

  /**
   * Unregister a view
   */
  def unregister(view:BufferView): Unit = {
    val bs = views.getOrElse(view.buffer, Nil)
    val vs = views - view.buffer
    val nbs = bs.filter(_ != view)

    views = vs.updated(view.buffer, nbs)

    if (nbs == Nil)
      onEmptyBuffer(view.buffer)
  }

  /* def isShared(view:BufferView): Unit */

  def onEmptyBuffer(buffer:Buffer):Unit
}
