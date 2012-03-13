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

import org.vipervm.platform.Processor
import org.vipervm.runtime._

trait LoadBalancingPolicy extends RankingPolicy {

  val loadBalancingCoef = 4.0f

  override def rankProcessor(task:Task,proc:Processor,current:Float):Float = {
    val r = 1.0f / (queues(proc).size.toFloat + 1.0f)
    super.rankProcessor(task, proc, current + r * loadBalancingCoef)
  }

}


