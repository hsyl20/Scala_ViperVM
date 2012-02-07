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
import org.vipervm.platform.opencl.OpenCLProcessor
import org.vipervm.runtime._
import org.vipervm.utils._

class DefaultScheduler(platform:Platform) extends Scheduler(platform) with Logging {

  /* Create a worker per processor */
  private val workers = platform.processors.map(new Worker(_,this))
  private var events:Map[Task,Event] = Map.empty

  start

  def act = loop {
    react {
      case SubmitTask(task,deps) => {
        val ev = new UserEvent
        events += (task -> ev)

        EventGroup(deps:_*).willTrigger {

          /* Select worker */
          val w = workers.filter(_.canExecute(task)).head

          info("[DefaultScheduler] Submit task %s to worker %s".format(task,w))

          /* Submit task */
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

}
