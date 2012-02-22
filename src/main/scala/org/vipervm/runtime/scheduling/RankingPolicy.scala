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

trait RankingPolicy extends DefaultScheduler {

  def rankWorker(task:Task,worker:Worker,current:Float):Float = current

  override def selectWorker(workers:Seq[Worker],task:Task):Worker = {
    val rankedWorker = (workers zip workers.map(w => rankWorker(task,w,1.0f))).sortBy(_._2).reverse
    rankedWorker.head._1
  }

}

