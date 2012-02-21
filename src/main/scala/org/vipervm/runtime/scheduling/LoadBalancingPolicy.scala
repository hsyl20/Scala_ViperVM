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

import org.vipervm.runtime._
import org.vipervm.runtime.scheduling.Messages.LoadStatus

trait LoadBalancingPolicy extends RankingPolicy {

  val loadBalancingCoef = 1.0f

  private def rankStatus(status:LoadStatus):Float = {
    1.0f/status.taskCount.toFloat
  }

  override def rankWorker(task:Task)(worker:Worker):Float = {
    val r = rankStatus(worker.loadStatus)
    (r * loadBalancingCoef) + super.rankWorker(task)(worker)
  }

}


