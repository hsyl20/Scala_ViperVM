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

package fr.hsyl20.vipervm.apps

import scala.swing._

object Profiler {
  def main(args:Array[String]): Unit = {
    val frame = new ProfilerFrame
    frame.visible = true
  }
}

class ProfilerFrame extends Frame {
  title = "Scala demo"
  contents = new Button {
    text = "Pouche mi!"
  }
}
