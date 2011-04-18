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
import scala.concurrent.Lock

/**
 * Events are handles on asynchronously executed commands
 *
 * Events can only be triggered once.
 */
trait Event {

  private var actors:List[Actor] = Nil
  protected var completed:Boolean = false
  private val lock = new Lock

  /**
   * Return true if this event completed
   */
  def test : Boolean = completed

  /**
   * Synchronously wait for an event to complete
   */
  def syncWait: Unit

  /**
   * Add an actor to the list of actors to notify when the event completes.
   * A message is sent immediately if event is already completed
   */
  def willNotify(actor:Actor): Unit = {
    lock.acquire

    if (!completed)
      actors ::= actor

    val perform = completed
    lock.release
    
    if (perform)
      actor ! EventComplete(this)
  }

  /**
   * Add a callback method that will be called when the event completes
   */
  def willTrigger[T](f: => T) = new FutureEvent[T](this, f)

  /**
   * Like willTrigger but folds FutureEvent hierarchy
   */
  def fold[T](f: => FutureEvent[T]): FutureEvent[T] = new FutureEvent[T](this, f.apply)

  /**
   * Implementations should call this method when the event completes
   */
  def complete: Unit = {
    lock.acquire
    completed = true
    lock.release

    for (a <- actors)
      a ! EventComplete(this)

    actors = Nil
  }
}

/**
 * Message sent to registered actors when the event completes
 */
case class EventComplete[E<:Event](event:E)

