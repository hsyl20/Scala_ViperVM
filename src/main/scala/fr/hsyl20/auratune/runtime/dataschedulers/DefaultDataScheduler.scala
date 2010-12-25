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

package fr.hsyl20.auratune.runtime.dataschedulers

import scala.collection.mutable.{Map,HashMap}
import fr.hsyl20.auratune.runtime._

class DefaultDataScheduler(runtime:Runtime) extends DataScheduler {

  protected var pendingStates: Map[DataState, DataStateEvent] = HashMap.empty

  def makeState(state:DataState): DataStateEvent = {

    /* Memory nodes to choose from */
    val ds = if (state.included.isEmpty) runtime.platform.memoryNodes else state.included
    val nodes = ds.filterNot(state.excluded.contains _)

    /* Check if this state is compatible with the current ones */
    //TODO
    //if ()
    /* Active this state  */
    //else
    /* Put the state in the queue */

    /* Prepare the event that will be returned if everything
     * goes well
     */
    val dse = new DataStateEvent {
      def memoryNode = memoryNode
      def dataState = state
    }

    /* Select memory node */
    val memoryNode = selectMemoryNode(state, nodes)

    /* Allocate and transfer data */
    val invalids = invalidData(state,memoryNode)
    for (d <- invalids) {
      /* Allocate buffers */
      //TODO
      /* If there is not enough estimated free memory or
       * if allocation fails, we need to remove buffers.
       * This may involve data transfer to host and may
       * not be possible now (=> pending data configuration)
       */
      //TODO
      /* Update data with newly allocated buffers in invalid state */
      //TODO
      /* Some buffers are already allocated but need to be updated
       * from a valid buffer (ReadOnly, ReadWrite accesses)
       */
      //TODO
      /* Callback to trigger the data configuration event once
       * every data transfer event has completed
       */
      //TODO
    }

    /* Add the data configuration to the list of active configuration */
    //TODO

    /* Add a callback for the scheduler to be notified when
     * the active data configuration is released
     */
    dse.dataState.addReleaseCallback(releaseDataState _)

    /* Return data state event */
    dse
  }

  /**
   * Give a score for the datastate on the given memory node
   *
   * This higher it is, the most likely the data configuration will
   * be set on the memory node
   */
  protected def score(state:DataState, memoryNode:MemoryNode):Float = {
    val invalid = invalidData(state,memoryNode)
    val sizeToTransfer = invalid.map(_.sizeOn(memoryNode)).sum

    sizeToTransfer.toFloat * (-0.001f) + 10.0f * state.priorities.getOrElse(memoryNode, 0)
  }

  /**
   * Return the list of data from the data configuration that are not
   * in a valid state on the given memory node
   */
  protected def invalidData(state:DataState, memoryNode:MemoryNode): Seq[Data] = {
    state.data.map(_._1).flatMap(d => d.status(memoryNode) match {
        case None 
          | Some(Data.Invalid) => Some(d)
        case _ => None
    })
  }

  /**
   * Select the best memory node for the given state
   *
   * By default, memory node with best score is selected
   */
  protected def selectMemoryNode(state:DataState, nodes:Seq[MemoryNode]): MemoryNode = {
    (nodes zip nodes.map(m => score(state,m))).sortBy(_._2).last._1
  }

  /**
   * Indicate that a data configuration is no longer required
   */
  protected def releaseDataState(state:DataState): Unit = {
    /* Release access locks on data of the configuration*/
    for (d <- state.data) {
      //TODO
    }
    /* Check for pending configurations that can be enabled */
    //TODO
  }
}
