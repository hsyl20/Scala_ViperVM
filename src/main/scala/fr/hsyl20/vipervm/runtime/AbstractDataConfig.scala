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
 * A DataState represents a configuration of Data
 */
abstract class AbstractDataConfig {

  protected val releaseEvent: UserEvent = new UserEvent

  /**
   * Release data state on event completion
   */
  def releaseOn(event:Event): Unit = {
    event.addCallback(_ => release)
  }

  /**
   * Release the Data State
   *
   * Indicate that Data in this data state can be released, that
   * buffers can be moved or released, etc.
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
}
