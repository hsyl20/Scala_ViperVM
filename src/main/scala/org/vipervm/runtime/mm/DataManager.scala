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

import org.vipervm.platform.{Platform,MemoryNode,Event,Data,FutureEvent}

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait DataManager {
  
  type DataConfig = Seq[(Data,MemoryNode)]

  def platform:Platform

  /** Prepare the given configuration */
  def prepare(config:DataConfig):Event

  /** Release the given configuration */
  def release(config:DataConfig):Unit

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
