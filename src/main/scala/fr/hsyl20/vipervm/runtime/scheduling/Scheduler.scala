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
 * Scheduler
 *
 * Schedulers have the responsability of ensuring progression
 * of the computation, load-balancing, etc.
 */
trait Scheduler {
  
  /**
   * Give a task for the scheduler to schedule after every
   * dependency has completed
   *
   * @return An event indicating task completion
   */
  def schedule(task:Task, dependencies:Seq[Event] = Nil): Event 

}
*/
