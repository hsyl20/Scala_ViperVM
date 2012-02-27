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

import org.vipervm.profiling.SVGProfiler
import scala.swing._

class ProfilerFrame(root:Component) extends MainFrame {
  title = "ViperVM Profiler"
  contents = root
}

object Profiler {
  def main(args:Array[String]): Unit = {
  }

  def dynamicRendering(profiler:SVGProfiler):Unit = {
    val canvas = Component.wrap(profiler.canvas)
    canvas.visible = true
    val frame = new ProfilerFrame(canvas)
    frame.size = new Dimension(800,600)
    frame.visible = true
  }
}


