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

import org.vipervm.platform.{Platform,MemoryNode,Event,Data,EventGroup,FutureEvent,UserEvent}
import org.vipervm.profiling._

class DefaultDataManager(val platform:Platform, profiler:Profiler = DummyProfiler) extends DataManager {

  protected var pendingConfigs:Map[DataConfig,UserEvent] = Map.empty

  protected def prepareInternal(config:DataConfig):Event = {

    val invalidData = config.filterNot { case (data,mem) => data.viewIn(mem).isDefined }

    new EventGroup( invalidData.map { case (data,memory) => {
      /* Allocate required buffers and views */
      //FIXME: support "no space left on device" exception
      val view = data.allocate(memory)

      /* Test if the view is read or written into */
      if (data.isDefined) {

        /* Schedule required data transfer to update the view */
        val sources = data.views
        val directSources = sources.filter(src => platform.linkBetween(src,view).isDefined)

        /* Select source and link */
        //FIXME: We need to support multi-hop links
        val source = directSources.headOption.getOrElse {
          throw new Exception("No direct source for one data. Multi-hop links not implemented (todo)")
        }
        val link = platform.linkBetween(source,view).getOrElse(
          throw new Exception("No direct link between data. Multi-hop links not implemented (todo)")
        )
        
        val transfer = link.copy(source,view)
        profiler ! DataTransferStart(data,transfer)

        /* Schedule data-view association */
        val assocEvent = new UserEvent
        transfer.willTrigger {
          profiler ! DataTransferEnd(data,transfer)
          data.store(view)
          assocEvent.complete
        }

        FutureEvent(view, assocEvent)
      }
      else {
        data.store(view)
        FutureEvent(view)
      }
    }})
  }

  protected def releaseInternal(config:DataConfig):Unit = { }

}
