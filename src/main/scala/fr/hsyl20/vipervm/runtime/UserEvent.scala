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

package fr.hsyl20.vipervm.runtime

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
