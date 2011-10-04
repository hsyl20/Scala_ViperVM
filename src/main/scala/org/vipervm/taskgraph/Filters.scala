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

package org.vipervm.taskgraph

/**
 * A data filter
 */
abstract class Filter {
  def name:String
  def desc(d:Data):ArrayDesc
}

/**
 * Matrix splitter: blocks of lines
 */
case class LineSplit(n:Int) extends Filter {

  def apply(d:Data) = {
    val f = new FilteredData(d,this)
    for (i <- 1 to n) yield new DataSelect(f,i)
  }

  def name = "LineSplit x"+n

  def desc(d:Data) = d.desc match {
    case MatrixDesc(am,an,typ) => ArrayDesc(1,Seq(n),MatrixDesc(am,an/n,typ))
    case _ => throw new Exception("Filter %s not applicable to this data type (%s)".format(this,d.desc))
  }
}

/**
 * Matrix splitter: blocks of columns
 */
case class ColumnSplit(n:Int) extends Filter {

  def apply(d:Data) = {
    val f = new FilteredData(d,this)
    for (i <- 1 to n) yield new DataSelect(f,i)
  }

  def name = "ColumnSplit x"+n

  def desc(d:Data) = d.desc match {
    case MatrixDesc(am,an,typ) => ArrayDesc(1,Seq(n),MatrixDesc(am/n,an,typ))
    case _ => throw new Exception("Filter %s not applicable to this data type (%s)".format(this,d.desc))
  }
}

/**
 * Matrix splitter: blocks
 */
case class BlockSplit(n:Int,m:Int) extends Filter {

  def apply(d:Data) = {
    val f = new FilteredData(d,this)
    for (i <- 1 to n) yield (1 to m) map (j => new DataSelect(f,i,j))
  }

  def name = "BlockSplit (%d x %d)".format(n,m)

  def desc(d:Data) = d.desc match {
    case MatrixDesc(am,an,typ) => ArrayDesc(2,Seq(n,m),MatrixDesc(am/n,an/m,typ))
    case _ => throw new Exception("Filter %s not applicable to this data type (%s)".format(this,d.desc))
  }
}
