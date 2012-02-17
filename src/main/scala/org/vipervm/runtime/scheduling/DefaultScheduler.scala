/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.runtime.scheduling

import grizzled.slf4j.Logging
import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.runtime.scheduling.Messages._

class DefaultScheduler(val platform:Platform) extends Scheduler with Logging {

  /* Create a worker per processor */
  private val workers = platform.processors.map(new Worker(_,this))
  /* Events associated with task completion */
  private var events:Map[Task,Event] = Map.empty

  start

  def act = loop {
    react {
      case SubmitTask(task,deps) => {
        val ev = new UserEvent
        events += (task -> ev)

        EventGroup(deps:_*).willTrigger {
          val w = selectWorker(workers.filter(_.canExecute(task)), task)
          info("[DefaultScheduler] Submit task %s to worker %s".format(task,w))
          w ! ExecuteTask(task)
        }

        sender ! ev
      }

      case TaskComplete(task) => {
        val ev = events(task)
        events -= task

        info("[DefaultScheduler] Completed task %s".format(task))	
        ev.complete
      }

    }
  }

  /** 
   * Select the worker that will execute the task amongst valid workers (i.e. for which
   * there is at least one compatible kernel).
   */
  def selectWorker(workers:Seq[Worker],task:Task):Worker = workers.head

}
