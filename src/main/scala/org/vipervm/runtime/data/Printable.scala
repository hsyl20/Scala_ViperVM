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
 * Data that can be printed
 */
trait PrintableMetaView extends HostableMetaView {
  
  protected def hostPrint(view:ViewType,buffer:HostBuffer):String

  /** 
   * Return a string representing the data
   */
  def print(dataManager:DataManager):FutureEvent[String] = {
    onHost(dataManager)(hostPrint)
  }
}
