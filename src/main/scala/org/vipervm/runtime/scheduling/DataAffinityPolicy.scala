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

trait DataAffinityPolicy extends RankingPolicy {

  val dataAffinityCoef = 1.0f

  private def rankState(state:DataState):Float = state match {
    case DataUnavailable => -1.0f
    case DataAvailable => 1.0f
    case DataIncoming => 0.8f
    case DataOutgoing => 0.4f
  }

  override def rankWorker(task:Task)(worker:Worker):Float = {
    val r = task.params.map(x => rankState(worker.dataState(x))).sum
    (r * dataAffinityCoef) + super.rankWorker(task)(worker)
  }

}
