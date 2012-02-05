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

import scala.actors._
import scala.actors.Actor._
import scala.concurrent.Lock

class FutureEvent[T](value: =>T, event:Event) extends Event with Function0[T] {
  private var fvalue: Option[T] = None
  private var waiters: List[OutputChannel[T]] = Nil

  private val myAct = actor { loop { react {
    case EventComplete(_) => {
      fvalue = Some(value)
      waiters.foreach(_ ! fvalue.get)
      waiters = Nil
      complete
    }
    case Wait => fvalue match {
      case None    => waiters ::= sender
      case Some(v) => reply(v)
    }
  }}}

  event.willNotify(myAct)

  /**
   * Wait for the value to be computed and return it
   */
  def apply():T = (myAct !? Wait).asInstanceOf[T]

  override def syncWait:Unit = (myAct !? Wait)

  case object Wait
}

object FutureEvent {
  object DummyEvent extends Event {
    complete
  }

  def apply[T](value:T, event:Event = DummyEvent):FutureEvent[T] = new FutureEvent[T](value,event)

  implicit def any2future[T](a:T):FutureEvent[T] = FutureEvent(a)
}
