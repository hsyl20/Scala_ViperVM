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

package org.vipervm.runtime.profiling

/*/**
 * Allow registration of events with their associated date
 */
class Profiler {
  protected var _events:List[(ProfilingEvent,Long)] = Nil

  /**
   * Use this method to notify the profiler that an event has just happened
   */
  def notify(event:ProfilingEvent): Unit = synchronized {
    _events ::= (event -> System.currentTimeMillis)
  }

  /**
   * Retrieve list of (event,date)
   */
  def events = _events
}
*/
