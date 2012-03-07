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

package org.vipervm.runtime.mm

import org.vipervm.platform.{Platform,MemoryNode,Event,MetaView,FutureEvent}
import org.vipervm.runtime.mm.config._

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait DataManager {
  
  def platform:Platform

  def create:Data
  def release(data:Data):Unit

  def setType(data:Data,typ:VVMType):Unit
  def getType(data:Data):Option[VVMType]

  def setMetaData(data:Data,meta:MetaData):Unit
  def getMetaData(data:Data):Option[MetaData]

  /** Associate an instance to a data */
  def associate(data:Data,repr:Repr,instance:DataInstance):Unit

  /** Available instances in a given memory */
  def availableInstancesIn(data:Data,memory:MemoryNode):Seq[DataInstance]

  /** Prepare the given configuration */
  def scheduleConfig(config:DataConfig):Event

  /** Release the given configuration */
  def releaseConfig(config:DataConfig):Unit

  /** Return the state of a data in a memory */
  def dataState(data:Data,memory:MemoryNode):DataState
  def updateDataState(data:Data,memory:MemoryNode,state:DataState):Unit

  /** Indicate to the data manager that an event occured */
  def wakeUp:Unit

  /** Asynchronously perform an operation using a given configuration */
  def withConfig[A](config:DataConfig)(body: => A):FutureEvent[A]
}

object DataManager {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply[A<:DataManager](dataManager: =>A):DataManager = {
    factory.typedActorOf( TypedProps[A](classOf[DataManager], dataManager))
  }
}
