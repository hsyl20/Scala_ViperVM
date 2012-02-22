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

package org.vipervm.runtime

import scala.actors._
import org.vipervm.platform.{Platform,MemoryNode,Event,Data,FutureEvent}

abstract class DataManager extends Actor {

  type DataConfig = Seq[(Data,MemoryNode)]
  case class DataConfigRelease(config:DataConfig)
  case class DataConfigPrepare(config:DataConfig)

  def act = loop { react {
    case DataConfigPrepare(config) => sender ! prepareInternal(config)
    case DataConfigRelease(config) => releaseInternal(config)
  }}

  start

  /** Prepare the given configuration */
  def prepare(config:DataConfig):Event = (this !? DataConfigPrepare(config)).asInstanceOf[Event]

  /** Release the given configuration */
  def release(config:DataConfig):Unit = this ! DataConfigRelease(config)

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

  val platform:Platform
}

