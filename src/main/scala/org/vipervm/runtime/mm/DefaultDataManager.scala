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
import org.vipervm.profiling._


class DefaultDataManager(val platform:Platform, profiler:Profiler = DummyProfiler) extends DataManager {

  protected var dataStates:Map[(MemoryNode,Data),DataState] = Map.empty
  
  protected var configs:Map[DataConfig,(Int,Event)] = Map.empty

  protected def queryDataStateInternal(memory:MemoryNode,data:Data):DataState = {
    dataStates.getOrElse(memory -> data, DataState())
  }

  protected def updateDataStateInternal(memory:MemoryNode,data:Data,state:DataState):Unit = {
    dataStates += (memory -> data) -> state
    notification
  }

  protected def releaseInternal(config:DataConfig):Unit = {
    //TODO: check config state and release data...

    val (count,event) = configs(config)
    if (count == 1) configs -= config
    else configs += config -> (count-1 -> event)

    notification
  }

  protected def prepareInternal(config:DataConfig):Event = {
    val (count,event) = configs.getOrElse(config, (0,new UserEvent))
    configs += config -> (count+1 -> event)
    notification
    event
  }

  protected def reaction:Unit = {
    /* Skip already complete configs */
    val confs = configs.collect { case (conf,(_,ev)) if !ev.test => conf }

    val (completed,uncompleted) = confs.partition(isComplete)

    /* Signal valid configs */
    completed.foreach { conf =>
      val (_,event) = configs(conf)
      event.complete
    }

    val metaConf = (Set.empty[(Data,MemoryNode)] /: uncompleted.map(_.toSet))(_++_)

    /* Skip data available or being transferred */
    val metaConf2 = metaConf.filterNot { case (data,mem) => {
      val state = queryDataStateInternal(mem,data)
      state.available || state.uploading
    }}

    val (validData,scratchData) = metaConf2.partition(x => isValid(x._1))

    /* Allocate scratch data */
    scratchData.foreach { case (data,mem) => {
      val view = data.allocate(mem)
      data.store(view)
      val state = queryDataStateInternal(mem,data)
      updateDataStateInternal(mem, data, state.copy(available = true))
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
      queryDataStateInternal(platform.hostMemory,data).uploading
    }

    /* Schedule indirect transfers to the host */
    val indirectTransfers = findirects.map { case (data,mem) => {
      //FIXME: support "not enough space on device" exception
      val target = data.allocate(platform.hostMemory)
      val (source,link) = selectDirectSource(data,target)
      transfer(data,source,target,link)
    }}
  }

  def isComplete(config:DataConfig):Boolean = {
    config.map {
      case (data,mem) => queryDataStateInternal(mem,data).available
    }.reduceLeft(_&&_)
  }

  def isDirect(data:Data,memory:MemoryNode):Boolean = data.views.exists(
    view => platform.linkBetween(view.buffer.memory,memory).isDefined
  )

  def isValid(data:Data):Boolean = data.isDefined

  protected def transfer(data:Data,source:BufferView,target:BufferView,link:Link):DataTransfer = {
    val mem = target.buffer.memory
    val tr = link.copy(source,target)
    profiler ! DataTransferStart(data,tr)

    val state = queryDataStateInternal(mem,data)
    updateDataStateInternal(mem, data, state.copy(uploading = true))

    tr.willTrigger {
      profiler ! DataTransferEnd(data,tr)
      data.store(target.asInstanceOf[data.ViewType])
      val state = dataState(mem,data)
      updateDataState(mem, data, state.copy(uploading = false, available = true))
    }

    tr
  }

  def selectDirectSource(data:Data, target:BufferView):(BufferView,Link) = {
    val directSources = data.views.filter(src => platform.linkBetween(src,target).isDefined)
    val src = directSources.head
    val link = platform.linkBetween(src,target).get
    (src,link)
  }

}
