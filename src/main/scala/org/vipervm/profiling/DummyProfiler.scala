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

package org.vipervm.profiling

import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.runtime.mm.Data

import akka.actor.{TypedActor,ActorSystem,TypedProps}

private class DummyProfiler extends Profiler {

  def transferStart(data:Data,dataTransfer:DataTransfer,timestamp:Long):Unit = {}
  def transferEnd(data:Data,dataTransfer:DataTransfer,timestamp:Long):Unit = {}
  def taskAssigned(task:Task,proc:Processor,timestamp:Long):Unit = {}
  def taskStart(task:Task,kernel:Kernel,proc:Processor,timestamp:Long):Unit = {}
  def taskCompleted(task:Task,proc:Processor,timestamp:Long):Unit = {}
}

object DummyProfiler {
  
  protected val factory = TypedActor.get(ActorSystem())

  lazy private val actor = factory.typedActorOf( TypedProps(classOf[Profiler],new DummyProfiler))

  def apply():Profiler = actor

}
