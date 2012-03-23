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
import scala.collection.JavaConversions._
import java.util.IdentityHashMap

import akka.actor.TypedActor

case class DataInfo(typ:Option[VVMType],meta:Option[MetaData],instances:Seq[DataInstance])

case class DataCopy(data:Data,source:DataInstance,target:MemoryNode,link:Link,event:Event)


trait DefaultDataManager extends Runtime {

  /********************************************************
   * Buffer management
   ********************************************************/

  /**
   * Try to allocate a buffer in the given memory
   */
  def allocateBuffer(memory:MemoryNode,size:Long):Buffer = {
    memory.allocate(size)
  }
  
  /**
   * Release a buffer if it contains no active view
   */
  def releaseBuffer(buffer:Buffer):Unit = {
    //Check for active views
    if (bufferViews.get(buffer).isDefined)
      throw new Exception("Cannot release buffer: active views")

    val mem = buffer.memory
    mem.free(mem.get(buffer))
  }

  /********************************************************
   * View management
   ********************************************************/

  private var bufferViews:Map[Buffer,IdentityHashSet[BufferView]] = Map.empty

  /**
  * Register a view in the system
  *
  * Buffer containing registered views can't be released
  */
  def registerView(view:BufferView):Unit = {
    val views = registeredViews(view.buffer)
    views += view
    bufferViews += view.buffer -> views
  }

  /**
   * Unregister a view
   */
  def unregisterView(view:BufferView):Unit = {
    val views = registeredViews(view.buffer)
    views -= view
    if (views.isEmpty)
      bufferViews -= view.buffer
  }

  /**
   * Return registered views in the given buffer
   */
  def registeredViews(buffer:Buffer) = bufferViews.getOrElse(buffer, IdentityHashSet.empty)

  
  /**
   * Allocate buffer(s) and views compatible with the given views in the given memory
   */
  protected def allocateCompatibleViews(views:Seq[BufferView],memory:MemoryNode):Seq[BufferView] = {
    def compatibleViewSize(view:BufferView,align:Long) = {
      val size = view match {
        case BufferView1D(_,_,size) => size
        case BufferView2D(_,_,width,height,_) => width*height
        case BufferView3D(_,_,width,height,depth,_,_) => width*height*depth
      }
      size + (size % align)
    }

    def createCompatibleViewAt(buffer:Buffer,view:BufferView,offset:Long):BufferView = view match {
      case BufferView1D(_,_,size) => BufferView1D(buffer,offset,size)
      case BufferView2D(_,_,width,height,_) => BufferView2D(buffer,offset,width,height,0)
      case BufferView3D(_,_,width,height,depth,_,_) => BufferView3D(buffer,offset,width,height,depth,0,0)
    }

    val align = 8L //TODO: correctly handle padding depending on device
    val sizes = views.map(v => compatibleViewSize(v,align))
    val totalSize = sizes.sum

    //TODO: check max allocatable size
    val buffer = memory.allocate(totalSize)

    val offsets = sizes.scanLeft(0L)(_ + _)

    (views zip offsets) map { case (v,o) => createCompatibleViewAt(buffer,v,o) }
  }

  /**
   * Transfer a view content into another view
   */
  protected def transferView(source:BufferView,target:BufferView,link:Link):DataTransfer = {
    val mem = target.buffer.memory
    val tr = link.copy(source,target)
    profiler.transferStart(tr)

    tr.willTrigger {
      profiler.transferEnd(tr)
      self.viewTransferCompleted(tr)
    }

    tr
  }


  /********************************************************
   * Storage management
   ********************************************************/

  /**
   * Allocate a new storage in given memory, compatible with given storage 
   */
  protected def allocateStorage(storage:Storage,memory:MemoryNode):Storage = {
    val views = allocateCompatibleViews(storage.views, memory)

    storage.duplicate(views)
  }

  protected def transferStorage(source:Storage,target:Storage,link:Link):Seq[DataTransfer] = {
    (source.views zip target.views).map { case (s,d) => transferView(s,d,link) }
  }


  /********************************************************
   * Data management
   ********************************************************/

  protected var datas:IdentityHashMap[Data,DataInfo] = new IdentityHashMap

  def createData:Data = {
    val data = new Data(this)
    datas += data -> DataInfo(None,None,Seq.empty)
    data
  }

  def releaseData(data:Data):Unit = {
    datas -= data
  }

  def setDataType(data:Data,value:VVMType):Unit = {
    val info = datas(data)
    datas += data -> info.copy(typ = Some(value))
  }

  def getDataType(data:Data):Option[VVMType] = datas(data).typ

  def setDataMeta(data:Data,value:MetaData):Unit = {
    val info = datas(data)
    datas += data -> info.copy(meta = Some(value))
  }

  def getDataMeta(data:Data):Option[MetaData] = datas(data).meta

  def associateDataInstance(data:Data,instance:DataInstance):Unit = {
    val info = datas(data)
    datas += data -> info.copy(instances = info.instances :+ instance)
  }

  /**
   * Indicate whether an instance of data is present in memory
   */
  def isAvailableIn(data:Data,memory:MemoryNode):Boolean = {
    !instancesIn(data,memory).isEmpty
  }

  /**
   * Return available instances of data in memory
   */
  def instancesIn(data:Data,memory:MemoryNode):Seq[DataInstance] = {
    datas(data).instances.filter(_.isAvailableIn(memory))
  }


  /********************************************************
   * Transfer management
   ********************************************************/
  protected var activeTransfers:Map[Data,IdentityHashSet[DataCopy]] = Map.empty

  /**
   * Current transfers for the given data
   */
  def currentTransfers(data:Data):IdentityHashSet[DataCopy] = activeTransfers.getOrElse(data, IdentityHashSet.empty)

  def viewTransferCompleted(transfer:DataTransfer):Unit = {
  }

  def transferCompleted(copy:DataCopy):Unit = {
    val current = currentTransfers(copy.data)
    current -= copy
    if (current.isEmpty)
      activeTransfers -= copy.data
  }

  /**
   * Allocate then duplicate or transfer a data instance into the given memory using the given link
   */
  def directCopy(data:Data,source:DataInstance,memory:MemoryNode,link:Link):FutureEvent[DataInstance] = {
    val targetStorage = allocateStorage(source.storage,memory)
    val transfers = transferStorage(source.storage,targetStorage,link)
    val instance = source.copy(storage = targetStorage)
    val copyEvent = new EventGroup(transfers)
    
    val dataCopy = DataCopy(data,source,memory,link,copyEvent)
    val current = currentTransfers(data)
    current += dataCopy
    activeTransfers += data -> current

    copyEvent.willTrigger {
      self.transferCompleted(dataCopy)
    }

    FutureEvent(instance, copyEvent)
  }

  protected def isDirect(data:Data,memory:MemoryNode):Boolean = {
    datas(data).instances.exists(_.storage.views.forall(
      view => platform.linkBetween(view.buffer.memory,memory).isDefined
    ))
  }


}
