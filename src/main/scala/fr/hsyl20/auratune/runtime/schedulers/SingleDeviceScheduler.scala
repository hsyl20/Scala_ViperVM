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

package fr.hsyl20.auratune.runtime.schedulers

import scala.collection.mutable.{Map,HashMap}
import scala.collection.immutable.{Map => ImmMap}

import scala.concurrent.Lock

import fr.hsyl20.auratune.runtime._

class SingleDeviceScheduler(device:Device, runtime:Runtime) extends Scheduler {
  
  /* Enqueued tasks associated to remaining dependencies */
  private var queuedTasks:Map[Task, List[Event]] = HashMap.empty

  /* Event triggered on task completion */
  private var taskEvents:Map[Task, UserEvent] = HashMap.empty

  /* Active tasks */
  private var activeLock: Lock = new Lock
  private var _activeTasks:ImmMap[Task,ScheduledTask] = ImmMap.empty


  def activeTasks = _activeTasks

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

    /* Create datastate with task Data on specified device */
    val ds = new DataState(task.data) {
      val includeDevices = List(device)
    }

    /* Schedule data configuration */
    val dsEvent = runtime.dataScheduler.makeState(ds)

    /* Release data state on task event completion */
    ds.releaseOn(event)

    /* Execute kernel on data state event completion */
    dsEvent.addCallback(dse => {
      /* Get kernel for selected device */
      val k = selectKernel(task, dse.device, dse.memoryNode)

      val st = new ScheduledTask(task, k, dse.device, dse.memoryNode)

      /* Execute task */
      val devEvent = st.execute

      /* Add scheduled task to the list of active tasks */
      activeLock.synchronized {
        _activeTasks = _activeTasks + (task -> st)
      }

      /* Trigger task event on devEvent completion */
      event.chainWith(devEvent)

      /* Remove from active task on completion */
      devEvent.addCallback(_ => activeLock.synchronized {
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
   * Select a kernel from the task's kernel set to execute on the given device
   *
   * Default behavior is to choose the first one compatible with the device
   */
  protected def selectKernel(task:Task, device:Device, memoryNode:MemoryNode): Kernel = {
    val params = task.args.map(_._1.toKernelParameter(memoryNode))
    val ks = for (k <- task.kernels if k.canExecute(device, params)) yield k
    if (ks.isEmpty)
      error("No device can execute the given task!")
    ks.head
  }
}
