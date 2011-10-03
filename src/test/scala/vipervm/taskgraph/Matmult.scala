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

package org.vipervm.tests

import org.vipervm.taskgraph._

class MatmultTask(a:Data,b:Data,c:Data) extends Task("matmult", Seq(a,b,c)) {
  def lineSplit = {
    val ls = new LineSplit
    val as = ls(a)
    val cs = ls(c)
    val tasks = (as zip cs) map (t => new MatmultTask(t._1, b, t._2))
    new TaskGraph(tasks, Nil)
  }

  override val splits = IndexedSeq(lineSplit _)

}

