/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**                     GPLv3
*/

package fr.hsyl20.auratune.apps

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
