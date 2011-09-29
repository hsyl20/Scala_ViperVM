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
class LineSplit extends Filter {
  val n = 10

  def apply(d:Data) = {
    val f = new FilteredData(d,this)
    for (i <- 1 to n) yield new DataSelect(f,i)
  }

  def name = "LineSplit x"+n
}
