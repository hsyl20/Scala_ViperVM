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

import akka.actor.{TypedActor,ActorSystem,TypedProps}

import grizzled.slf4j.Logging

private class SLF4JProfiler extends Profiler with Logging {

  def transferStart(data:MetaView,dataTransfer:DataTransfer,timestamp:Long):Unit = {
    info("Starting transfer of %s from memory %s to memory %s using link %s".format(
      data,dataTransfer.source.buffer.memory,dataTransfer.target.buffer.memory,dataTransfer.link)
    )
  }

  def transferEnd(data:MetaView,dataTransfer:DataTransfer,timestamp:Long):Unit = {
    info("Transfer of %s from memory %s to memory %s using link %s completed".format(
      data,dataTransfer.source.buffer.memory,dataTransfer.target.buffer.memory,dataTransfer.link)
    )
  }

  def taskAssigned(task:Task,proc:Processor,timestamp:Long):Unit = {
    info("Task %s assigned to processor %s".format(task,proc))
  }

  def taskStart(task:Task,kernel:Kernel,proc:Processor,timestamp:Long):Unit = {
    info("Execution of task %s started by processor %s using kernel %s".format(task,proc,kernel))
  }

  def taskCompleted(task:Task,proc:Processor,timestamp:Long):Unit = {
    info("Task %s completed by processor %s".format(task,proc))
  }
}

object SLF4JProfiler {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply():Profiler = {
    factory.typedActorOf( TypedProps(classOf[Profiler],new SLF4JProfiler))
  }

}
