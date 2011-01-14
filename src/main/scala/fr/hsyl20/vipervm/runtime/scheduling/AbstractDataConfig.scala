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

package fr.hsyl20.vipervm.runtime.scheduling

/*/**
 * A data configuration.
 *
 * Data configurations are requests for the data scheduler.
 */
abstract class AbstractDataConfig {

  protected val releaseEvent: UserEvent = new UserEvent

  /**
   * Release this data configuration on event completion
   */
  def releaseOn(event:Event): Unit = {
    event.addCallback(_ => release)
  }

  /**
   * Release this data configuration
   *
   * The requester of this data configuration has to release it
   * when it becomes useless.
   */
  final def release: Unit = {
    if (!releaseEvent.test) {
      releaseEvent.trigger
    }
  }

  /**
   * Add a callback triggered on release of this data configuration
   */
  def addReleaseCallback(f:this.type=>Unit): Unit = {
    releaseEvent.addCallback(_ => f(this))
  }
}*/
