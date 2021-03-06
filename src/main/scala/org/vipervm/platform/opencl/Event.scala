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

package org.vipervm.platform.opencl

import org.vipervm.platform.{Event,EventPollingThread}
import org.vipervm.bindings.{opencl => cl}

class OpenCLEvent(val peer:cl.Event) extends Event {
  override def syncWait: Unit = peer.syncWait

  override def test:Boolean = {
    if (peer.completed)
      complete
    completed
  }

  //TODO:we should only use polling with OpenCL 1.0. Callbacks are supported as of OpenCL 1.1
  //TODO: ATI CPU implementation doesn't perform asynchronous data transfers if no synchronous wait is performed...
  EventPollingThread.monitorEvent(this)
}
