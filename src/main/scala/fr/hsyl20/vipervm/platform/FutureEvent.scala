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

import scala.actors._
import scala.actors.Actor._
import scala.concurrent.Lock

class FutureEvent[T](event:Event, f: =>T) extends Event with Function0[T] {
  private var fvalue: Option[T] = None
  private var waiters: List[OutputChannel[T]] = Nil

  private val myAct = actor { loop { react {
    case EventComplete(_) => {
      fvalue = Some(f)
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

  def syncWait:Unit = (myAct !? Wait)

  case object Wait
}

object FutureEvent {
  object DummyEvent extends Event {
    def syncWait:Unit = ()

    complete
  }

  def constant[T](a:T):FutureEvent[T] = new FutureEvent[T](DummyEvent, a)

  implicit def any2future[T](a:T):FutureEvent[T] = constant(a)
}
