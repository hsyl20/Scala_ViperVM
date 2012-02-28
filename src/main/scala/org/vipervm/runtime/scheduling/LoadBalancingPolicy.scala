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

  val loadBalancingCoef = 4.0f

  private def rankStatus(status:LoadStatus):Float = {
    1.0f / (status.taskCount.toFloat + 1.0f)
  }

  override def rankWorker(task:Task,worker:Worker,current:Float):Float = {
    val r = rankStatus(worker.loadStatus)
    super.rankWorker(task, worker, current + r * loadBalancingCoef)
  }

}


