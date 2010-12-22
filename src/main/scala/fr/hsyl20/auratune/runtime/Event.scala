/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**                     GPLv3
*/

package fr.hsyl20.auratune.runtime

import scala.actors._

/**
 * Events are handles on asynchronously executed commands
 *
 * Events can only be triggered once.
 */
abstract class Event {

  private var actors:List[Actor] = Nil
  private var callbacks:List[this.type=>Unit] = Nil
  private var completed:Boolean = false

  /**
   * Return true if this event completed
   */
  def test : Boolean = completed

  /**
   * Synchronously wait for an event to complete
   */
  //TODO
  //def wait: Unit

  /**
   * Add an actor to the list of actors to notify when the event completes.
   * A message is sent immediately if event is already completed
   */
  def notify(actor:Actor): Unit = actors ::= actor

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
  protected def complete: Unit = {
    completed = true

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

