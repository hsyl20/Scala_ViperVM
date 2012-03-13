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

import org.vipervm.platform.{Platform,DataTransfer,Processor,MemoryNode}
import org.vipervm.runtime.interpreter.{DefaultInterpreter,Term}
import org.vipervm.library.Library
import org.vipervm.profiling._
import org.vipervm.runtime.mm.{DataManager,Data}
import org.vipervm.runtime.scheduling._

import akka.actor.TypedActor

class DefaultRuntime(library:Library,dataManager:DataManager,profiler:Profiler = DummyProfiler()) extends Runtime {

  private val self = TypedActor.self[Runtime]

  val platform = dataManager.platform

  protected var toBeComputed:Set[Data] = Set.empty
  protected var pendingTasks:Map[Task,Set[Data]] = Map.empty
  protected var queues:Map[Processor,Set[Task]] = Map.empty

  protected def rewrite(term:Term):Option[Term] = {
    None
  }

  protected def submit(task:Task):Unit = {
    val invalidData = task.params.filter(d => toBeComputed.contains(d)).toSet
    pendingTasks += task -> invalidData
    wakeUp
  }

  protected def wakeUp:Unit = {
    if (!pendingTasks.isEmpty) {
      
      val w = selectProcessor(platform.processors.filter(_.canExecute(task)), task)
      w.executeTask(task)
    }
  }


  /** 
   * Select the worker that will execute the task amongst valid workers (i.e. for which
   * there is at least one compatible kernel).
   */
  def selectProcessor(processors:Seq[Processor],task:Task):Processor = processors.head

  def idleProcessor(proc:Processor):Unit = {
  }

  def allocate(memory:MemoryNode,data:Data,repr:Repr):DataInstance = {
  }

  def computedData(data:Data):Unit = {
    toBeComputed -= data
    pendingTasks.mapValues(_.filter(_!=data))
  }

  def completedTask(task:Task,proc:Processor):Unit = {
    profiler.taskCompleted(task,proc)

    kernelCompleted(task.kernel)
    computedData(task.result)

    val tasks = queues(proc)
    queues += proc -> (tasks - task)

    idleProcessor(proc)
  }

  def selectFunction(task:Task):Function = {
  }

  def startTransfer(data:Data,memory:MemoryNode,repr:Repr):Unit = {
  }

  def executeTask(task:Task,proc:Processor):Unit = {

    val func = selectFunction(task)
    val kernel = func.Kernel.getKernelsFor(proc).head

    /* Schedule kernel execution */
    val ev = proc.execute(kernel, task.makeKernelParams(memory))

    profiler.taskStart(task,kernel,proc)

    /* Schedule notification after kernel execution */
    ev.willTrigger {
      self.completedTask(task)

      dataManager.releaseConfig(memConf)
    }
  }

}

object DefaultRuntime {

  def apply(library:Library,dataManager:DataManager,profiler:Profiler=DummyProfiler()):Runtime = {
    Runtime {
      new DefaultRuntime(library,dataManager,profiler)
    }
  }

}
