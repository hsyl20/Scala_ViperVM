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

import org.vipervm.platform.{Platform,DataTransfer,KernelEvent,Processor}
import org.vipervm.runtime.interpreter.Term
import org.vipervm.runtime.mm.{DataManager,Data}
import org.vipervm.library.Library

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait Runtime {
  val platform:Platform
  val library:Library
  val dataManager:DataManager

  protected val queues:Map[Processor,Set[Task]]

  def rewrite(term:Term):Option[Term]
  def submit(task:Task):Unit

  protected def transferCompleted(transfer:DataTransfer):Unit
  protected def kernelCompleted(kernel:KernelEvent):Unit
}


object Runtime {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply[A<:Runtime](runtime: =>A):Runtime = {
    factory.typedActorOf( TypedProps[A](classOf[Runtime], runtime))
  }
}
