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

import org.vipervm.platform.{Processor,UserEvent,FutureEvent,EventGroup,MetaView,MemoryNode}
import org.vipervm.runtime._
import org.vipervm.runtime.mm._
import org.vipervm.profiling._

import org.vipervm.utils._

import akka.actor.TypedActor


trait Worker {
  val self = TypedActor.self[Worker]

  def loadStatus:LoadStatus
  def dataState(data:MetaView):DataState
  def executeTask(task:Task):Unit
  def completedTask(task:Task):Unit
  def canExecute(task:Task):Boolean
}

/**
 * Defaut worker (typed actor)
 */
class DefaultWorker(proc:Processor,scheduler:Scheduler,profiler:Profiler,dataManager:DataManager) extends Worker with Serializable {

  import TypedActor.dispatcher

  var tasks:List[Task] = Nil
  var currentTask:Option[Task] = None

  val memory:MemoryNode = proc.memory

  def dataState(data:MetaView):DataState = dataManager.dataState(data,memory)

  def loadStatus:LoadStatus = LoadStatus(tasks.length + (if (currentTask.isDefined) 1 else 0))

  def executeTask(task:Task):Unit = {
      profiler.taskAssigned(task,proc)

      if (currentTask.isDefined) {
        tasks ::= task
      }
      else {
        executeTaskInternal(task)
      }
  }

  def completedTask(task:Task):Unit = {
    assert(task == currentTask.get)
    
    profiler.taskCompleted(task,proc)

    scheduler.completedTask(task)

    /* Execute another task, if any */
    currentTask = None
    tasks match {
      case t :: l => {
        tasks = l
        executeTaskInternal(t)
      }
      case Nil => ()
    }
  }

  def executeTaskInternal(task:Task):Unit = {
    currentTask = Some(task)


    /* Select kernel */
    val kernel = task.kernel.getKernelsFor(proc).head

    val memConf = task.kernel.memoryConfig(task.params,memory,scheduler.platform.hostMemory)

    val event = dataManager.prepare(memConf)

    val me = self

    event.willTrigger {

      /* Schedule kernel execution */
      val ev = proc.execute(kernel, task.makeKernelParams(memory))

      profiler.taskStart(task,kernel,proc)

      /* Schedule notification after kernel execution */
      ev.willTrigger {
        me.completedTask(task)

        dataManager.release(memConf)
      }
    }
  }

  def canExecute(task:Task):Boolean = task.canExecuteOn(proc)
}

