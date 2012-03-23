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

import org.vipervm.platform.{Platform,DataTransfer,KernelExecution,Processor}
import org.vipervm.runtime.interpreter.Term
import org.vipervm.runtime.mm._
import org.vipervm.library.Library
import org.vipervm.profiling.Profiler

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait Runtime {
  val platform:Platform
  val library:Library
  val profiler:Profiler

  protected lazy val self = TypedActor.self[Runtime]

  protected var queues:Map[Processor,Set[Task]]

  def submit(task:Task):Unit

  def transferCompleted(transfer:DataCopy):Unit
  def viewTransferCompleted(transfer:DataTransfer):Unit
  def kernelCompleted(kernelEvent:KernelExecution):Unit
  def wakeUp:Unit


  def createData:Data
  def releaseData(data:Data):Unit

  def setDataType(data:Data,typ:VVMType):Unit
  def getDataType(data:Data):Option[VVMType]

  def setDataMeta(data:Data,meta:MetaData):Unit
  def getDataMeta(data:Data):Option[MetaData]

  def associateDataInstance(data:Data,instance:DataInstance):Unit

  def selectProcessor(procs:Seq[Processor],task:Task):Processor
}


object Runtime {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply[A<:Runtime](runtime: =>A):Runtime = {
    factory.typedActorOf( TypedProps[A](classOf[Runtime], runtime))
  }
}
