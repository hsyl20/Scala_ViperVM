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

trait RankingPolicy extends Runtime {

  def rankProcessor(task:Task,proc:Processor,current:Float):Float = current

  override def selectProcessor(procs:Seq[Processor],task:Task):Processor = {
    val rankedProcs = (procs zip procs.map(w => rankProcessor(task,w,1.0f))).sortBy(_._2).reverse
    rankedProcs.head._1
  }

}

