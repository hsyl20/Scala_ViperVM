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

class Marker extends Task {
  val name = "marker"
  val args = Nil
}

class Reduction(op:(Data,Data,Data) => Task,as:Seq[Data],res:Data) extends Task {
  private val dummy = InitialData(DummyDesc,NoInit,"dummy")

  private val o = op(dummy,dummy,dummy)
  val name = "reduction_" + o.name
  val args = as :+ res
}
