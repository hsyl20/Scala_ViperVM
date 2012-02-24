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

import scala.actors._
import org.vipervm.platform.{Platform,MemoryNode,Event,Data,FutureEvent}

abstract class DataManager extends Actor {

  type DataConfig = Seq[(Data,MemoryNode)]
  case class DataConfigRelease(config:DataConfig)
  case class DataConfigPrepare(config:DataConfig)
  case class QueryDataState(memory:MemoryNode,data:Data)
  case class UpdateDataState(memory:MemoryNode,data:Data,state:DataState)
  case object Notification

  def act = loop { react {
    case DataConfigPrepare(config) => sender ! prepareInternal(config)
    case DataConfigRelease(config) => releaseInternal(config)
    case QueryDataState(memory,data) => sender ! queryDataStateInternal(memory,data)
    case UpdateDataState(memory,data,state) => updateDataStateInternal(memory,data,state)
    case Notification => reaction
  }}

  start

  /** Prepare the given configuration */
  def prepare(config:DataConfig):Event = (this !? DataConfigPrepare(config)).asInstanceOf[Event]

  /** Release the given configuration */
  def release(config:DataConfig):Unit = this ! DataConfigRelease(config)

  /** Return the state of a data in a memory */
  def dataState(memory:MemoryNode,data:Data):DataState = (this !? QueryDataState(memory,data)).asInstanceOf[DataState]

  protected def updateDataState(memory:MemoryNode,data:Data,state:DataState):Unit = this ! UpdateDataState(memory,data,state)

  /** Indicate to the data manager that an event occured */
  protected def notification:Unit = this ! Notification

  /** Asynchronously perform an operation using a given configuration */
  def withConfig[A](config:DataConfig)(body: => A):FutureEvent[A] = {
    prepare(config) willTrigger {
      val result = body
      release(config)
      result
    }
  }

  protected def prepareInternal(config:DataConfig):Event
  protected def releaseInternal(config:DataConfig):Unit
  protected def queryDataStateInternal(memory:MemoryNode,data:Data):DataState
  protected def updateDataStateInternal(memory:MemoryNode,data:Data,state:DataState):Unit
  protected def reaction:Unit


  val platform:Platform
}

