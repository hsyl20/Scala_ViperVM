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

package org.vipervm.runtime.scheduling

import org.vipervm.runtime.{Task,Data}
import org.vipervm.platform.{MemoryNode,Processor}

/**
 * Manage data configurations
 */
trait DataManager extends ActorFunctionalScheduler {

  protected var memoryData:Map[MemoryNode,Set[Data]] = Map.empty
  /* Indicate how many tasks/transfers use a given data */
  protected var dataLocks: Map[(MemoryNode,Data),Int] = Map.empty


  /**
   * Put locks on data used by the scheduled task
   */
  override protected def schedule(task:Task,proc:Processor,mem:MemoryNode):Unit = {
    //TODO: check that data are present in memory

    for (d <- task.input union task.output) {
      val old = dataLocks.getOrElse((mem,d), 0)
      dataLocks = dataLocks.updated((mem,d), old + 1)
    }

    super.schedule(task,proc,mem)
  }


  /**
   * Remove locks on data used by the completed task
   */
  override def onTaskCompleted(task:Task,proc:Processor,mem:MemoryNode):Unit = {
    for (d <- task.input union task.output) {
      val old = dataLocks((mem,d))
      if (old - 1 == 0)
        dataLocks = dataLocks - ((mem,d))
      else
        dataLocks = dataLocks.updated((mem,d), old - 1)
    }

    super.onTaskCompleted(task,proc,mem)
  }


  /**
   * Return the set of available data on a given memory node
   */
  def dataOn(memory:MemoryNode): Set[Data] = memoryData.getOrElse(memory, Set.empty)

  /**
   * Retrieve data that aren't used by any data config on a node
   */
  def unusedOn(memory:MemoryNode):Set[Data] = dataOn(memory).filterNot(d => isUsedOn(d,memory))

  /**
   * Indicate whether a data is used on a memory node
   * If the data isn't present on the node, return false.
   */
  def isUsedOn(data:Data,memory:MemoryNode): Boolean = dataLocks.getOrElse((memory,data), 0) != 0
}
