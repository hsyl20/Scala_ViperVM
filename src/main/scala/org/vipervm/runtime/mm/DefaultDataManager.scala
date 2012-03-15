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

import org.vipervm.platform._
import org.vipervm.runtime.mm.config._
import org.vipervm.profiling._
import org.vipervm.utils._
import org.vipervm.runtime.Runtime

import scala.collection.immutable.HashMap

import akka.actor.TypedActor

case class DataCopy(source:DataInstance,target:MemoryNode)

trait DefaultDataManager extends Runtime {

  protected var types:Map[Data,VVMType] = Map.empty
  protected var metadata:Map[Data,MetaData] = Map.empty
  protected var instances:Map[Data,Seq[DataInstance]] = Map.empty
  protected var events:Map[Data,Event] = Map.empty

  protected var activeTransfers:Map[Data,DataCopy] = Map.empty


  def createData:Data = {
    val d = new Data(this)
    instances += d -> Seq.empty
    d
  }

  def releaseData(data:Data):Unit = {
    types -= data
    metadata -= data
    instances -= data
  }

  def setDataType(data:Data,typ:VVMType):Unit = {
    types += data -> typ
  }

  def getDataType(data:Data):Option[VVMType] = types.get(data)

  def setDataData(data:Data,meta:MetaData):Unit = {
    metadata += data -> meta
  }

  def getDataData(data:Data):Option[MetaData] = metadata.get(data)

  def associateDataInstance(data:Data,instance:DataInstance):Unit = {
    val insts = availableInstances(data)
    instances += data -> (insts :+ instance)
  }

  def availableInstances(data:Data):Seq[DataInstance] = instances.getOrElse(data, Seq.empty)

  /**
   * Transfer a view content into another view
   */
  protected def transfer(source:BufferView,target:BufferView,link:Link):DataTransfer = {
    val mem = target.buffer.memory
    val tr = link.copy(source,target)
    profiler.transferStart(tr)

    tr.willTrigger {
      profiler.transferEnd(tr)
      self.transferCompleted(tr)
    }

    tr
  }

  /**
   * Allocate a new storage in given memory, compatible with given storage 
   */
  protected def allocateStorage(storage:Storage,memory:MemoryNode):Storage = {
    val views = storage.views.map( _ match {
      case BufferView1D(_,offset,size) => {
        val buffer = memory.allocate(size)
        BufferView1D(buffer,0,size)
      }
      case BufferView2D(_,offset,width,height,padding) => {
        val buffer = memory.allocate(width*height)
        BufferView2D(buffer,0,width,height,0)
      }
      case BufferView3D(_,offset,width,height,depth,padding0,padding1) =>{
        val buffer = memory.allocate(width*height*depth)
        BufferView3D(buffer,0,width,height,depth,0,0)
      }
    })
    storage.duplicate(views)
  }

  protected def transferStorage(source:Storage,target:Storage,link:Link):Seq[DataTransfer] = {
    (source.views zip target.views).map { case (s,d) => transfer(s,d,link) }
  }

  /**
   * Allocate then duplicate or transfer a data instance into the given memory using the given link
   */
  def directCopy(data:DataInstance,memory:MemoryNode,link:Link):FutureEvent[DataInstance] = {
    val targetStorage = allocateStorage(data.storage,memory)
    val transfers = transferStorage(data.storage,targetStorage,link)
    val di = new DataInstance(data.typ,data.meta,data.repr,data.properties,targetStorage)
    FutureEvent(di, new EventGroup(transfers))
  }


  def isAvailableIn(data:Data,memory:MemoryNode):Boolean = {
    !availableInstancesIn(data,memory).isEmpty
  }

  def availableInstancesIn(data:Data,memory:MemoryNode):Seq[DataInstance] = {
    availableInstances(data).filter(_.isAvailableIn(memory))
  }


  def wakeUp:Unit = {
   
/*
    val metaConf = (Set.empty[(MetaView,MemoryNode)] /: uncompleted.map(_.toSet))(_++_)

    /* Skip data available or being transferred */
    val metaConf2 = metaConf.filterNot { case (data,mem) => {
      val state = dataStateInternal(data,mem)
      state.available || state.uploading
    }}

    val (validData,scratchData) = metaConf2.partition(x => isValid(x._1))

    /* Allocate scratch data */
    scratchData.foreach { case (data,mem) => {
      val view = data.allocate(mem)
      data.store(view)
      val state = dataStateInternal(data,mem)
      updateDataStateInternal(data, mem, state.copy(available = true))
    }}

    /* Split between those requiring a hop in host memory and the other */
    val (directs,indirects) = validData.partition {
     case (data,mem) => isDirect(data,mem)
    }

    /* Schedule required direct data transfers  */
    val directTransfers = directs.map { case (data,mem) => {
      //FIXME: support "not enough space on device" exception
      val target = data.allocate(mem)
      val (source,link) = selectDirectSource(data,target)
      transfer(data,source,target,link)
    }}

    /* Check that no uploading is taking place to the host for indirect transfers */
    val findirects = indirects.filterNot { case (data,_) =>
      dataStateInternal(data, platform.hostMemory).uploading
    }

    /* Schedule indirect transfers to the host */
    val indirectTransfers = findirects.map { case (data,mem) => {
      //FIXME: support "not enough space on device" exception
      val target = data.allocate(platform.hostMemory)
      val (source,link) = selectDirectSource(data,target)
      transfer(data,source,target,link)
    }}*/
  }

  def isComplete(config:DataConfig):Boolean = {
    /*config.map {
      case (data,mem) => dataStateInternal(data,mem).available
    }.reduceLeft(_&&_)*/
    ???
  }

  def isDirect(data:Data,memory:MemoryNode):Boolean = {
    availableInstances(data).exists(_.storage.views.forall(
      view => platform.linkBetween(view.buffer.memory,memory).isDefined
    ))
  }

  def isValid(data:Data):Boolean = ???//data.isDefined

  def selectDirectSource(data:MetaView, target:BufferView):(BufferView,Link) = {
    val directSources = data.views.filter(src => platform.linkBetween(src,target).isDefined)
    val src = directSources.head
    val link = platform.linkBetween(src,target).get
    (src,link)
  }

}
