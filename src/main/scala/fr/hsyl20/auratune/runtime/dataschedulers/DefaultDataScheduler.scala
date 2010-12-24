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

    /* Available devices */
    val ds = if (state.includedDevices.isEmpty) runtime.platform.devices else state.includedDevices
    val devices = ds.filterNot(state.excludedDevices.contains _)

    /* Select device */
    val (device,memoryNode) = selectDevice(state, devices)

    /* Check if this state is compatible with the current ones */
    //TODO
    //if ()
    /* Active this state  */
    //else
    /* Put the state in the queue */

    /* List data invalid on the memory node */
    val requiredData = (for ((d,mode) <- state.data) yield {
      d.status(memoryNode) match {
        case None => Some((d,mode))
        case Some(Data.Shared(_)) if mode != AccessMode.ReadOnly => Some((d,mode))
        case Some(Data.Invalid) => Some((d,mode))
        case _ => None
      }
    }).flatten

    

    val dse = new DataStateEvent {
      def device = device
      def memoryNode = memoryNode
      def dataState = state
    }

    dse.dataState.addReleaseCallback(releaseDataState _)

    /* Return data state event */
    dse
  }

  protected def selectDevice(state:DataState, devices:Seq[Device]): (Device,MemoryNode) = {
    (devices.head, devices.head.memoryNodes.head)
  }

  protected def releaseDataState(state:DataState): Unit = {
    for (d <- state.data) {
    }
  }
}
