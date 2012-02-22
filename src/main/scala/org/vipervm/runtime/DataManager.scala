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

import org.vipervm.platform.{Platform,MemoryNode,Event,Data,EventGroup,FutureEvent,UserEvent,Storage}
import org.vipervm.profiling._

class DataManager(val platform:Platform, profiler:Profiler = DummyProfiler) {

  /** Prepare a memory node for the given kernel and parameters */
  def prepare(config:Seq[(Data,MemoryNode)]):Event = {

    val invalidData = config.filterNot { case (data,mem) => data.viewIn(mem).isDefined }

    new EventGroup( invalidData.map { case (data,memory) => {
      /* Allocate required buffers and views */
      //FIXME: support "no space left on device" exception
      val view = data.allocate(memory)

      /* Test if the view is read or written into */
      if (data.isDefined) {

        /* Schedule required data transfer to update the view */
        val sources = data.views

        /* Select source */
        val source = sources.head._2

        /* Select link */
        //FIXME: We need to support multi-hop links
        val link = platform.linkBetween(source,view).get
        
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

  def release(config:Seq[(Data,MemoryNode)]):Unit = {
  }

  def withConfig[A](config:Seq[(Data,MemoryNode)])(body: => A):FutureEvent[A] = {
    prepare(config) willTrigger {
      val result = body
      release(config)
      result
    }
  }
}
