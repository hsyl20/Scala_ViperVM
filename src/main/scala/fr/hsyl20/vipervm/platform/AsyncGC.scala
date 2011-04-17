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

import scala.collection.mutable.HashMap

/**
 * Keep track of JVM data that should not be released until an event is triggered
 */
object AsyncGC {
  private val table: HashMap[Event,List[Any]] = HashMap.empty

  def add(event:Event, items:Any*): Unit = {
    table.synchronized {
      table(event) = items.toList ::: table.getOrElse(event,Nil)
    }

    event.addCallback(remove _)
  }

  private def remove(event:Event): Unit = {
    table.synchronized {
      table.remove(event)
      table.notify
    }
  }

  override def finalize:Unit = {
    table.synchronized {
      while (!table.isEmpty)
        table.wait
    }
  }
}
