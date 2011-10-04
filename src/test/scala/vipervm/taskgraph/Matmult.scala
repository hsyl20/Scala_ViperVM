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
import scalaz._
import Scalaz._

class MatMul(val a:Data, val b:Data, val c:Data) extends Task{
  val name = "matmul"
  val args = Seq(a,b,c)


  def lineSplit = {
    val ls = new LineSplit(10)
    val as = ls(a)
    val cs = ls(c)
    val tasks = (as zip cs) map (t => new MatMul(t._1, b, t._2))
    new TaskGraph(tasks, Nil)
  }

  def columnSplit = {
    val cols = new ColumnSplit(10)
    val bs = cols(b)
    val cs = cols(c)
    val tasks = (bs zip cs) map (t => new MatMul(a, t._1, t._2))
    new TaskGraph(tasks, Nil)
  }

  def blockSplit:TaskGraph = {
    val blocks = new BlockSplit(3,3)
    val as = blocks(a)
    val bs = blocks(b) //TODO: should be b.transpose
    val cs = blocks(c)
    def red(as:Seq[Data],bs:Seq[Data],c:Data) = {
      val lc = as zip bs
      val mulTasks = lc map (a => new MatMul(a._1,a._2, new TemporaryData(a._1.desc)))
      val red = new Reduction((a,b,c) => new MatAdd(a,b,c), mulTasks.map(_.c), c)
      val deps = mulTasks.map(t => (red,t))
      new TaskGraph(red +: mulTasks, deps)
    }
    val linecols = (as.zipWithIndex <|*|> bs.zipWithIndex)
    val gs = linecols.map(a => red(a._1._1, a._2._1, cs(a._1._2)(a._2._2)))
    new TaskGraph(gs.flatMap(_.tasks), gs.flatMap(_.deps))
  }

  override val splits = IndexedSeq(
    lineSplit _,
    columnSplit _,
    blockSplit _
    )

}

class MatAdd(a:Data,b:Data,c:Data) extends Task {
  val name = "matadd"
  val args = Seq(a,b,c)
}
