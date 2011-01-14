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

package fr.hsyl20.vipervm.runtime.scheduling

/*
import scala.collection.mutable.{Map,HashMap}
import scala.collection.immutable.{Map => ImmMap}

import scala.concurrent.Lock

import fr.hsyl20.vipervm.runtime._

class SingleDeviceScheduler(proc:Processor, runtime:Runtime) extends Scheduler {
  
  /* Enqueued tasks associated to remaining dependencies */
  private var queuedTasks:Map[Task, List[Event]] = HashMap.empty

  /* Event triggered on task completion */
  private var taskEvents:Map[Task, UserEvent] = HashMap.empty

  /* Active tasks */
  private var activeLock: Lock = new Lock
  private var _activeTasks:ImmMap[Task,ScheduledTask] = ImmMap.empty


  def runningTasks = _activeTasks.keys.toSeq

  def schedule(task:Task, dependencies:List[Event] = Nil): Event = {

    /* Create event to return */
    val event = new UserEvent

    if (dependencies.isEmpty)
      /* We bypass the queue and prepare it directly */
      prepare(task, event)
    else {
      taskEvents.synchronized {
        taskEvents += (task -> event)
      }

      /* Put the task in the queue */
      queuedTasks.synchronized {
        queuedTasks += (task -> dependencies)
      }

      /* Add callbacks */
      for (d <- dependencies) {
        d.addCallback(eventCallback(task))
      }
    }

    event
  }

  /**
   * Callback called when an event on which "task" depends is completed
   */
  protected def eventCallback(task:Task)(event:Event): Unit = {
    /* Remove completed event from task dependencies */
    val newEvents = queuedTasks.synchronized[List[Event]] {
      val oldEvents = queuedTasks(task)
      val newEvents = oldEvents filterNot (_ == event)
      if (newEvents.isEmpty) queuedTasks.remove(task) else queuedTasks.update(task, newEvents)
      newEvents
    }

    /* If no dependency left, prepare the task */
    if (newEvents.isEmpty)
      prepare(task, popTaskEvent(task))
  }

  /**
   * Prepare the given task's data
   */
  protected def prepare(task:Task, event:UserEvent): Unit = {

    /* Create data configuration where
     *  - data are task parameters
     *  - memory node to choose from are those associated with proc
     */
    val config = new DataConfig(task.data) {
      override val included = proc.memories
    }

    /* Schedule data configuration */
    val dcEvent = runtime.dataScheduler.configure(config)

    /* Release data state on task event completion */
    config.releaseOn(event)

    /* Execute kernel when data configuration is ready */
    dcEvent.addCallback(dce => {
      val proc = runtime.platform.processorsFor(dce.memoryNode).head

      /* Get kernel for selected processor */
      val k = selectKernel(task, proc, dce.memoryNode)

      val st = new ScheduledTask(task, k, proc, dce.memoryNode)

      /* Execute kernel */
      val runningKernel = st.execute

      /* Add scheduled task to the list of active tasks */
      activeLock.synchronized {
        _activeTasks = _activeTasks + (task -> st)
      }

      /* Trigger task event on devEvent completion */
      event.chainWith(runningKernel.event)

      /* Remove from active task on completion */
      runningKernel.event.addCallback(_ => activeLock.synchronized {
        _activeTasks = _activeTasks - task
      })
    })
  }

  protected def popTaskEvent(task:Task): UserEvent = {
    taskEvents.synchronized[UserEvent] {
      val e = taskEvents(task)
      taskEvents.remove(task)
      e
    }
  }

  /**
   * Select a kernel from the task's kernel set to be executed by the given processor
   *
   * Default behavior is to choose the first one compatible with the processor
   */
  protected def selectKernel(task:Task, proc:Processor, memoryNode:MemoryNode): Kernel = {
    val params = task.args.map(_._1.toKernelParameter(memoryNode))
    val ks = for (k <- task.kernels ; confK = ConfiguredKernel(k,params) if proc.canExecute(confK)) yield k
    if (ks.isEmpty)
      error("No device can execute the given task!")
    ks.head
  }
}

*/
