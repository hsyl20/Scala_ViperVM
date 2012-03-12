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

import org.vipervm.runtime.interpreter.Term
import org.vipervm.runtime.Task

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait Runtime {
  val platform:Platform
  val library:Library
  val dataManager:DataManager

  def rewrite(term:Term):Option[Term]
  def submit(task:Task):Unit

  def transferCompleted(transfer:DataTransfer):Unit
  def kernelCompleted(kernel:KernelEvent):Unit
}


object Runtime {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply[A<:Runtime](runtime: =>A):Runtime = {
    factory.typedActorOf( TypedProps[A](classOf[Runtime], runtime))
  }
}
