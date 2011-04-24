/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.platform.opencl

import fr.hsyl20.vipervm.platform.{Event,EventPollingThread}
import fr.hsyl20.{opencl => cl}

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
