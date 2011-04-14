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

/**
 * Events are handles on asynchronously executed commands
 *
 * Events can only be triggered once.
 */
abstract class Event {

  private var actors:List[Actor] = Nil
  private var callbacks:List[this.type=>Unit] = Nil
  protected var completed:Boolean = false

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
  def addActor(actor:Actor): Unit = actors ::= actor

  /**
   * Add a callback method that will be called when the event completes
   *
   * Blocking or time-consuming callbacks should be avoided. Use actors
   * and notify method instead
   */
  def addCallback(f:(this.type) => Unit): Unit = callbacks ::= f

  /**
   * Implementations should call this method when the event completes
   */
  def complete: Unit = {

    this.synchronized {
      completed = true
      this.notifyAll
    }

    for (a <- actors)
      a ! EventComplete(this)

    for (c <- callbacks)
      c(this)
  }
}

/**
 * Message sent to registered actors when the event completes
 */
case class EventComplete(event:Event)

