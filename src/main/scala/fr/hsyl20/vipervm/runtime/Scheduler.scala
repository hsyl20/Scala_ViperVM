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
 * Task scheduler
 *
 * Schedulers have the responsability of ensuring progression
 * of the computation, load-balancing, etc.
 */
abstract class Scheduler {
  
  /**
   * Give a task for the scheduler to schedule after every
   * dependency has completed
   *
   * @return An event indicating task completion
   */
  def schedule(t:Task, dependencies:List[Event] = Nil): Event 
}
