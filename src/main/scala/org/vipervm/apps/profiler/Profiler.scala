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

package org.vipervm.apps

import scala.swing._

object Profiler {
  private class ProfilerFrame extends MainFrame {
    title = "Scala demo"
    contents = new Button {
      text = "Pouche mi!"
    }
  }

  def main(args:Array[String]): Unit = {
    val frame = new ProfilerFrame
    frame.visible = true
  }
}


