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

import org.vipervm.platform.{Platform,DataTransfer,Processor,MemoryNode,Kernel,KernelExecution}
import org.vipervm.runtime.interpreter.{DefaultInterpreter,Term}
import org.vipervm.library.Library
import org.vipervm.profiling._
import org.vipervm.runtime.mm._
import org.vipervm.runtime.scheduling._

import akka.actor.TypedActor

class DefaultRuntime(val platform:Platform, val library:Library, val profiler:Profiler = DummyProfiler()) extends Runtime with DefaultDataManager {

  protected var toBeComputed:Set[Data] = Set.empty
  protected var pendingTasks:Set[Task] = Set.empty
  protected var queues:Map[Processor,Set[Task]] = Map.empty

  protected var runningKernels:Map[Kernel,Task] = Map.empty

  def submit(task:Task):Unit = {
    pendingTasks += task
    wakeUp
  }

  def wakeUp:Unit = {
    // Tasks with valid parameters
    val tasks = pendingTasks.filter(_.params.filter(d => toBeComputed.contains(d)).isEmpty)

    if (!tasks.isEmpty) {
      val task = tasks.head
      
      val procs = platform.processors.filter( proc =>
        task.functions.exists( func => func.kernel.canExecuteOn(proc)))

      val proc = selectProcessor(procs,task)
      val kernel = selectKernel(proc,task)
      proc.execute(kernel)
    }
  }

  /** 
   * Select the worker that will execute the task amongst valid workers (i.e. for which
   * there is at least one compatible kernel).
   */
  def selectProcessor(processors:Seq[Processor],task:Task):Processor = processors.head

  def selectKernel(proc:Processor,task:Task):Kernel = {
    val kernels = task.functions.map(_.kernel)
    kernels.head
  }

  def computedData(data:Data):Unit = {
    toBeComputed -= data
  }

  def kernelCompleted(kernelEvent:KernelExecution):Unit = {
    val task = runningKernels(kernelEvent.kernel)
    val proc = kernelEvent.processor
    
    profiler.taskCompleted(task,proc)
    computedData(task.result)

    val tasks = queues(proc)
    queues += proc -> (tasks - task)
  }

  def executeTask(task:Task,proc:Processor):Unit = {

    val func = task.functions.filter(_.kernel.canExecuteOn(proc)).head
    val kernel = func.kernel
    val memory = proc.memory

    /* Schedule kernel execution */
  /*  val ev = proc.execute(kernel, func.kernel.makeKernelParams(memory))

    profiler.taskStart(task,kernel,proc)

    /* Schedule notification after kernel execution */
    ev.willTrigger {
      self.completedTask(task)

      dataManager.releaseConfig(memConf)
    }*/
  }

}

object DefaultRuntime {

  def apply(platform:Platform,library:Library,profiler:Profiler=DummyProfiler()):Runtime = {
    Runtime {
      new DefaultRuntime(platform,library,profiler)
    }
  }

}
