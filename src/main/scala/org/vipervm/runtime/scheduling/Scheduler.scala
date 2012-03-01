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

import org.vipervm.platform.{Event,Platform}
import org.vipervm.runtime.Task
import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait Scheduler {
  def submitTask(task:Task,deps:Seq[Event]):Event
  def completedTask(task:Task):Unit

  def platform:Platform
}

object Scheduler {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply[A<:Scheduler](scheduler: =>A):Scheduler = {
    factory.typedActorOf( TypedProps[A](classOf[Scheduler],scheduler))
  }
}
