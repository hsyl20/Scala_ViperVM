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

import fr.hsyl20.vipervm.runtime.{Task,Data}
import fr.hsyl20.vipervm.platform.{MemoryNode,Processor,DataTransfer,BufferView}
import scala.actors.Actor
import scala.actors.Actor._

/**
 * Functional scheduler implemented with actors
 */
abstract class ActorFunctionalScheduler extends Actor with FunctionalScheduler {

  /**
   * Signal to the scheduler that an task has been enqueued
   */
  override def submit(task:Task): Unit = {
    this ! TaskSubmitted(task)
  }

  /**
   * Signal to the scheduler that some data have been discarded
   */
  override def discard(data:Data*): Unit = {
    this ! DataDiscarded(data.toList)
  }

  /**
   * Dispatch events to methods that can be overloaded
   */
  protected val dispatch: PartialFunction[Any,Unit] = {
    case TaskSubmitted(task) => onTaskSubmitted(task)
    case TaskCompleted(task,proc,memory) => onTaskCompleted(task,proc,memory)
    case DataDiscarded(data) => onDataDiscarded(data)
    case DataTransferCompleted(transfer) => onDataTransferCompleted(transfer)
  }

  def act() { loop { react { dispatch } } }

  /**
   * Called when an task has been enqueued
   */
  def onTaskSubmitted(task:Task): Unit = {}

  /**
   * Called when an task has completed execution
   */
  def onTaskCompleted(task:Task,proc:Processor,memory:MemoryNode): Unit = {}

  /**
   * Indicate that a data won't be used anymore by future submitted tasks
   */
  def onDataDiscarded(data:Seq[Data]): Unit = {}

  /**
   * Called when a data transfer has completed
   */
  def onDataTransferCompleted[V <: BufferView](transfer:DataTransfer[V]): Unit = {}

  /** Actor messages */
  protected case class TaskSubmitted(task:Task)
  protected case class TaskCompleted(task:Task, proc:Processor, memory:MemoryNode)
  protected case class DataDiscarded(data:Seq[Data])
  protected case class DataTransferCompleted[V <: BufferView](transfer:DataTransfer[V])
}


