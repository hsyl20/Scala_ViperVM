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
}

/**
 * Matrix splitter: blocks of lines
 */
class LineSplit(n:Int) extends Filter {

  def apply(d:Data) = {
    val f = new FilteredData(d,this)
    for (i <- 1 to n) yield new DataSelect(f,i)
  }

  def name = "LineSplit x"+n
}

/**
 * Matrix splitter: blocks of columns
 */
class ColumnSplit(n:Int) extends Filter {

  def apply(d:Data) = {
    val f = new FilteredData(d,this)
    for (i <- 1 to n) yield new DataSelect(f,i)
  }

  def name = "ColumnSplit x"+n
}

/**
 * Matrix splitter: blocks
 */
class BlockSplit(n:Int,m:Int) extends Filter {

  def apply(d:Data) = {
    val f = new FilteredData(d,this)
    for (i <- 1 to n) yield (1 to m) map (j => new DataSelect(f,i,j))
  }

  def name = "BlockSplit (%d x %d)".format(n,m)
}
