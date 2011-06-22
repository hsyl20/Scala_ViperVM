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

import org.vipervm.runtime.{Task,Data}
import org.vipervm.platform.{MemoryNode,Processor,DataTransfer,BufferView}

/**
 * Manage data on each memory node
 */
trait DataManager extends ActorFunctionalScheduler {

  protected var memoryData:Map[MemoryNode,Set[Data]] = Map.empty
  /* Indicate how many tasks/transfers use a given data */
  protected var dataLocks: Map[(MemoryNode,Data),Int] = Map.empty
  /* Active data transfers */
  protected var dataTransfers: Map[MemoryNode, Map[Data, DataTransfer[_]]] = Map.empty
  /* Active data duplications */
  //TODO
  //protected var dataDuplications:

  /**
   * Put locks on data used by the scheduled task
   */
  override protected def schedule(task:Task,proc:Processor,mem:MemoryNode):Unit = {
    //TODO: check that data are present in memory

/*    for (d <- task.input union task.output) {
      val old = dataLocks.getOrElse((mem,d), 0)
      dataLocks = dataLocks.updated((mem,d), old + 1)
    }*/

    super.schedule(task,proc,mem)
  }


  /**
   * Remove locks on data used by the completed task
   */
  override protected def onTaskCompleted(task:Task,proc:Processor,mem:MemoryNode):Unit = {
/*    for (d <- task.input union task.output) {
      val old = dataLocks((mem,d))
      if (old - 1 == 0)
        dataLocks = dataLocks - ((mem,d))
      else
        dataLocks = dataLocks.updated((mem,d), old - 1)
    }*/

    super.onTaskCompleted(task,proc,mem)
  }

  /**
   * Schedule a data transfer from source to target for data
   */
  protected def transfer(data:Data,source:BufferView,target:BufferView):Unit = {
    //TODO: check source and target
    //TODO: check that no transfer is already running

    val link = platform.linkBetween(source, target)
    val dt = link match {
      case Some(l) => l.copy(source,target)
      case None => {
        /* TODO: Use host mem for intermediate data transfer */
        sys.error("todo")
      }
    }

    val memTransfers = dataTransfers.getOrElse(target.buffer.memory, Map.empty)
    val newTransfers = memTransfers.updated(data, dt)
    dataTransfers = dataTransfers.updated(target.buffer.memory, newTransfers)

    dt willTrigger {
      data.views
      //this ! DataTransferCompleted(
      //TODO
    }
  }


  /**
   * Return the set of available data on a given memory node
   */
  protected def dataOn(memory:MemoryNode): Set[Data] = memoryData.getOrElse(memory, Set.empty)

  /**
   * Retrieve data that aren't used by any data config on a node
   */
  protected def unusedOn(memory:MemoryNode):Set[Data] = dataOn(memory).filterNot(d => isUsedOn(d,memory))

  /**
   * Indicate whether a data is used on a memory node
   * If the data isn't present on the node, return false.
   */
  protected def isUsedOn(data:Data,memory:MemoryNode): Boolean = dataLocks.getOrElse((memory,data), 0) != 0
}
