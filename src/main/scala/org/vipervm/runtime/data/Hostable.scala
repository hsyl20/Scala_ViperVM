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

package org.vipervm.runtime.data

import org.vipervm.platform._
import org.vipervm.runtime.mm.DataManager


/**
 * MetaView that can be used on host
 */
trait HostableMetaView extends MetaView {
  
  def onHost[A](dataManager:DataManager)(body: (ViewType,HostBuffer) => A):FutureEvent[A] = {
    val platform = dataManager.platform
    val memConf = Seq(this -> platform.hostMemory)

    dataManager.withConfig(memConf) {
      val view = viewIn(platform.hostMemory).get
      val buf = view.buffer.asInstanceOf[HostBuffer]

      body(view,buf)
    }
  }
}
