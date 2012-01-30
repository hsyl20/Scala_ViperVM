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

import scala.actors.{Actor,OutputChannel}

import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.utils._

class DefaultScheduler(platform:Platform) extends Scheduler(platform) {

  /* Create a worker per processor */
  private val workers = platforms.processor.map(new Worker(_,this))

  start

  def act = loop {
    react {
      case SubmitTask(task,deps) => ???
    }
  }

}
