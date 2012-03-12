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

import scala.collection.immutable.HashMap

import akka.actor.TypedActor

private class DefaultDataManager(val platform:Platform, profiler:Profiler) extends DataManager {

  private val self = TypedActor.self[DataManager]

  protected var dataStates:Map[(MemoryNode,Data),DataState] = Map.empty
  
  protected var configs:Map[DataConfig,(Int,Event)] = Map.empty

  protected var types:Map[Data,VVMType] = Map.empty
  protected var metadata:Map[Data,MetaData] = Map.empty
  protected var instances:Map[Data,Seq[DataInstance]] = Map.empty
  protected var events:Map[Data,Event] = Map.empty

  def create:Data = {
    val d = new Data(this)
    instances += d -> Seq.empty
    d
  }

  def release(data:Data):Unit = {
    types -= data
    metadata -= data
    instances -= data
  }

  def setType(data:Data,typ:VVMType):Unit = {
    types += data -> typ
  }

  def getType(data:Data):Option[VVMType] = types.get(data)

  def setMetaData(data:Data,meta:MetaData):Unit = {
    metadata += data -> meta
  }

  def getMetaData(data:Data):Option[MetaData] = metadata.get(data)

  def associate(data:Data,instance:DataInstance):Unit = {
    val insts = availableInstances(data)
    instances += data -> (insts :+ instance)
  }

  def availableInstances(data:Data):Seq[DataInstance] = instances.getOrElse(data, Seq.empty)

  def isAvailableIn(data:Data,memory:MemoryNode):Boolean = {
    !availableInstancesIn(data,memory).isEmpty
  }

  def availableInstancesIn(data:Data,memory:MemoryNode):Seq[DataInstance] = {
    val instances = availableInstances(data).filter(_.isAvailableIn(memory) match {
      case Left(ds) => ds.map(d => isAvailableIn(d,memory)).reduce(_&&_)
      case Right(b) => b
    })
    instances
  }

  def getEvent(data:Data):Event = {
    events.getOrElse(data, {
      val e = new UserEvent
      events += data -> e
      if (!availableInstances(data).isEmpty) e.complete
      e
    })
  }


  def scheduleConfig(config:DataConfig):Event = {
    val (count,event) = configs.getOrElse(config, (0,new UserEvent))
    configs += config -> (count+1 -> event)
    self.wakeUp
    event
  }

  def releaseConfig(config:DataConfig):Unit = {
    //TODO: check config state and release data...

    val (count,event) = configs(config)
    if (count == 1) configs -= config
    else configs += config -> (count-1 -> event)

    self.wakeUp
  }

  def withConfig[A](config:DataConfig)(body: => A):FutureEvent[A] = {
    scheduleConfig(config) willTrigger {
      val result = body
      self.releaseConfig(config)
      result
    }
  }

  def dataState(data:Data,memory:MemoryNode):DataState = {
    dataStateInternal(data,memory)
  }

  protected def dataStateInternal(data:Data,memory:MemoryNode):DataState = {
    dataStates.getOrElse(memory -> data, DataState())
  }

  def updateDataState(data:Data,memory:MemoryNode,state:DataState):Unit = {
    updateDataStateInternal(data,memory,state)
  }

  protected def updateDataStateInternal(data:Data,memory:MemoryNode,state:DataState):Unit = {
    dataStates += (memory -> data) -> state
    self.wakeUp
  }


  def wakeUp:Unit = {
    /* Skip already complete configs */
/*    val confs = configs.collect { case (conf,(_,ev)) if !ev.test => conf }

    val (completed,uncompleted) = confs.partition(isComplete)

    /* Signal valid configs */
    completed.foreach { conf =>
      val (_,event) = configs(conf)
      event.complete
    }

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

  def isDirect(data:Data,memory:MemoryNode):Boolean = ???/*data.views.exists(
    view => platform.linkBetween(view.buffer.memory,memory).isDefined
  )*/

  def isValid(data:Data):Boolean = ???//data.isDefined

  protected def transfer(data:Data,source:BufferView,target:BufferView,link:Link):DataTransfer = {
/*    val mem = target.buffer.memory
    val tr = link.copy(source,target)
    profiler.transferStart(data,tr)

    val state = dataStateInternal(data,mem)
    updateDataStateInternal(data, mem, state.copy(uploading = true))

    tr.willTrigger {
      profiler.transferEnd(data,tr)
      data.store(target.asInstanceOf[data.ViewType])
      val state = dataState(data,mem)
      updateDataState(data, mem, state.copy(uploading = false, available = true))
    }

    tr*/
    ???
  }

  def selectDirectSource(data:MetaView, target:BufferView):(BufferView,Link) = {
    val directSources = data.views.filter(src => platform.linkBetween(src,target).isDefined)
    val src = directSources.head
    val link = platform.linkBetween(src,target).get
    (src,link)
  }

}

object DefaultDataManager {

  def apply(platform:Platform,profiler:Profiler = DummyProfiler()):DataManager = {
    DataManager {
      new DefaultDataManager(platform,profiler)
    }
  }

}
