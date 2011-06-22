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

import org.vipervm.runtime.Task
import org.vipervm.platform.{EventGroup,Platform,MemoryNode,Processor}

/**
 * Trait that notifies the scheduler when a task has its input data ready
 */
trait TaskDataWait extends ActorFunctionalScheduler {
  
  override def onTaskSubmitted(task:Task): Unit = {

    /* Wait for input data to be ready */
    val evGrp = new EventGroup(task.input.map(_.computedEvent))
    evGrp willTrigger {
      this ! TaskDataReady(task)
    }

    super.onTaskSubmitted(task)
  }

  protected case class TaskDataReady(task:Task)
  override def dispatch = super.dispatch orElse { case TaskDataReady(task) => onTaskDataReady(task) }

  /**
   * Called when task's data are ready (i.e. have been computed)
   */
  protected def onTaskDataReady(task:Task): Unit = {}
}
