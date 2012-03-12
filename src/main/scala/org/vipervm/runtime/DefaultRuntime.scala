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

package org.vipervm.runtime

import org.vipervm.interpreter.{Interpreter,Term}
import org.vipervm.library.Library
import org.vipervm.runtime.mm.{DataManager,Data}

import akka.actor.TypedActor

class DefaultRuntime(library:Library,dataManager:DataManager,profiler:Profiler = DummyProfiler()) extends Runtime {

  private val self = TypedActor.self[Runtime]

  val platform = dataManager.platform

  protected def rewrite(term:Term):Option[Term] = {
    None
  }

  protected def submit(task:Task):Unit = {

    tasks ::= task

    EventGroup(deps:_*).willTrigger {
      val w = selectWorker(workers.filter(_.canExecute(task)), task)
      w.executeTask(task)
    }

  }

  var tasks:List[Task] = Nil

  /* Create a worker per processor */
  private val workers = platform.processors.map(x => DefaultWorker(x,self,profiler,dataManager))

  /** 
   * Select the worker that will execute the task amongst valid workers (i.e. for which
   * there is at least one compatible kernel).
   */
  def selectWorker(workers:Seq[Worker],task:Task):Worker = workers.head

  def computedData(data:Data):Unit

}

object DefaultRuntime {

  def apply(library:Library,dataManager:DataManager,profiler:Profiler=DummyProfiler()):Runtime = {
    Runtime {
      new DefaultRuntime(library,dataManager,profiler)
    }
  }

}
