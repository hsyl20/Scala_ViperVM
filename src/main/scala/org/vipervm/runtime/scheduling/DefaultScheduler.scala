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

import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.runtime.scheduling.Messages._
import org.vipervm.runtime.mm.DataManager
import org.vipervm.profiling.{Profiler,DummyProfiler}

class DefaultScheduler(val dataManager:DataManager, val profiler:Profiler = DummyProfiler) extends Scheduler {

  val platform = dataManager.platform

  /* Create a worker per processor */
  private val workers = platform.processors.map(x => new Worker(x,this,profiler,dataManager))
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
          w ! ExecuteTask(task)
        }

        sender ! ev
      }

      case TaskComplete(task) => {
        val ev = events(task)
        events -= task

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
