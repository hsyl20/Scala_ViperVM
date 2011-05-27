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

import scala.actors.Actor
import scala.actors.Actor._

/**
 * An event that completes when all events it contains have completed
 *
 * @param events Events in this group
 */
class EventGroup[E <: Event](val events:Seq[E]) extends Event {

  private var remaining: Set[Any] = events.toSet

  private val myAct = actor {
    loopWhile(!events.isEmpty) {
      react {
        case EventComplete(event) => {
          remaining -= event
          if (events.isEmpty)
            complete
        }
      }
    }
  }

  events.foreach(_ willNotify myAct)
}

object EventGroup {
  implicit def seqev[E<:Event](evs:Seq[E]) = new {
    def group = new EventGroup[E](evs)
  }
}
