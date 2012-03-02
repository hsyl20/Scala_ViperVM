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
import org.vipervm.runtime.mm.DataManager
import org.vipervm.profiling.{Profiler,DummyProfiler}

import akka.actor.{TypedActor,ActorSystem,TypedProps}

class DefaultScheduler(val dataManager:DataManager, val profiler:Profiler = DummyProfiler()) extends Scheduler {

  val platform = dataManager.platform

  protected val system = ActorSystem("workers")
  protected val factory = TypedActor.get(system)

  /* Create a worker per processor */
  private val workers = platform.processors.map(x => {
    val worker:Worker = factory.typedActorOf(
      TypedProps[DefaultWorker](classOf[Worker],
        new DefaultWorker(x,this,profiler,dataManager)))
    worker
  })

  /* Events associated with task completion */
  private var events:Map[Task,Event] = Map.empty

  def submitTask(task:Task,deps:Seq[Event]):Event = {
    val ev = new UserEvent
    events += (task -> ev)

    EventGroup(deps:_*).willTrigger {
      val w = selectWorker(workers.filter(_.canExecute(task)), task)
      w.executeTask(task)
    }

    ev
  }

  def completedTask(task:Task):Unit = {
    val ev = events(task)
    events -= task

    ev.complete
  }


  /** 
   * Select the worker that will execute the task amongst valid workers (i.e. for which
   * there is at least one compatible kernel).
   */
  def selectWorker(workers:Seq[Worker],task:Task):Worker = workers.head

}

object DefaultScheduler {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply(dataManager:DataManager, profiler:Profiler = DummyProfiler()):Scheduler = {
    Scheduler {
      new DefaultScheduler(dataManager,profiler)
    }
  }

}
