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

/**
 * Event that can be triggered by user code.
 */
class UserEvent extends Event {
  
  /**
   * Trigger the event
   */
  def trigger: Unit = complete


  /**
   * Link this user event with the given one
   *
   * When "event" completes, trigger is called
   */
  def chainWith(event:Event): Unit = {
    event.addCallback(_ => trigger)
  }
}
