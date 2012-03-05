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

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait DataManager {
  
  type DataConfig = Seq[(MetaView,MemoryNode)]

  def platform:Platform

  /** Register a data in the system */
  def register(data:Data):Unit

  /** Associate an instance to a data */
  def associate(instance:DataInstance[Repr],data:Data):Unit

  /** Prepare the given configuration */
  def prepare(config:DataConfig):Event

  /** Release the given configuration */
  def release(config:DataConfig):Unit

  /** Return the state of a data in a memory */
  def dataState(data:MetaView,memory:MemoryNode):DataState
  def updateDataState(data:MetaView,memory:MemoryNode,state:DataState):Unit

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
