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
import org.vipervm.runtime.DataManager

/**
 * Data that can be printed
 */
trait PrintableData extends Data {
  
  protected def hostPrint(view:ViewType,buffer:HostBuffer):String

  /** 
   * Return a string representing the data
   */
  def print(dataManager:DataManager):FutureEvent[String] = {
    val platform = dataManager.platform
    val memConf = Seq(this -> platform.hostMemory)

    dataManager.withConfig(memConf) {
      val view = viewIn(platform.hostMemory).get
      val buf = view.buffer.asInstanceOf[HostBuffer]

      hostPrint(view,buf)
    }
  }
}
