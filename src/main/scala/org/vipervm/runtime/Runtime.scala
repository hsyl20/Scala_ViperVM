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
import org.vipervm.runtime.mm._
import org.vipervm.library.Library

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait Runtime {
  val platform:Platform
  val library:Library

  protected val queues:Map[Processor,Set[Task]]

  def rewrite(term:Term):Option[Term]
  def submit(task:Task):Unit

  protected def transferCompleted(transfer:DataTransfer):Unit
  protected def kernelCompleted(kernel:KernelEvent):Unit
  protected def wakeUp:Unit


  def createData:Data

  def setDataType(data:Data,typ:VVMType):Unit
  def getDataType(data:Data):Option[VVMType]

  def setDataMeta(data:Data,meta:MetaData):Unit
  def getDataMeta(data:Data):Option[MetaData]

  def associateDataInstance(data:Data,instance:DataInstance):Unit
}


object Runtime {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply[A<:Runtime](runtime: =>A):Runtime = {
    factory.typedActorOf( TypedProps[A](classOf[Runtime], runtime))
  }
}
