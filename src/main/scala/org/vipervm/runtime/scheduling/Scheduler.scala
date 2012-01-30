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

package org.vipervm.runtime.scheduling

import scala.actors.Actor
import org.vipervm.platform.{Event,Platform}
import org.vipervm.runtime.Task

abstract class Scheduler(val platform:Platform) extends Actor {

  def submitTask(task:Task,deps:Seq[Event]):Event = {
    (this !? SubmitTask(task,deps)).asInstanceOf[Event]
  }

}
