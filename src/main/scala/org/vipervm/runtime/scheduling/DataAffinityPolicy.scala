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

import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.runtime.mm._

trait DataAffinityPolicy extends RankingPolicy {

  val dataAffinityCoef = 1.0f

  private def rankState(state:DataState):Float = {
    if (!state.available && !state.uploading && state.futureUsers == 0) 0.0f
    else if (!state.available && !state.uploading && state.futureUsers != 0) 0.2f
    else if (!state.available && state.uploading) 0.5f
    else 1.0f
  }

  override def rankWorker(task:Task,worker:Worker,current:Float):Float = {
    val r = task.params.map(x => rankState(worker.dataState(x))).sum
    super.rankWorker(task, worker, current + r * dataAffinityCoef)
  }

}
