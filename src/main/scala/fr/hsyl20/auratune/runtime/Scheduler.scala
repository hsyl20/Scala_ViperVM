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
