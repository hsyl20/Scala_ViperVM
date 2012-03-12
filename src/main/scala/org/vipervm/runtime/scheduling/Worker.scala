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

import org.vipervm.platform.{Processor,UserEvent,FutureEvent,EventGroup,MetaView,MemoryNode}
import org.vipervm.runtime._
import org.vipervm.runtime.mm._
import org.vipervm.profiling._

import org.vipervm.utils._

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait Worker {
  val self = TypedActor.self[Worker]

  def loadStatus:LoadStatus
  def dataState(data:Data):DataState
  def executeTask(task:Task):Unit
  def completedTask(task:Task):Unit
  def canExecute(kernel:MetaKernel):Boolean
}

object Worker {
  protected val factory = TypedActor.get(ActorSystem("workers"))

  def apply[A<:Worker](worker: =>A):Worker = {
    factory.typedActorOf( TypedProps[A](classOf[Worker], worker))
  }

}
