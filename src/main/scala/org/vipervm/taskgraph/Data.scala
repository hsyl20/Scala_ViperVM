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

sealed abstract class Data {
  val desc:DataDesc
}
case class InitialData(desc:DataDesc,init:DataInit,name:String) extends Data
case class FilteredData(source:Data, filter:Filter) extends Data {
  val desc = filter.desc(source)
}
case class DataSelect(src:FilteredData, id:Int*) extends Data {
  val desc = src.desc.elem
}
case class TemporaryData(desc:DataDesc,id:Int) extends Data {
  def this(desc:DataDesc) = this(desc,TemporaryData.getId)
}

object TemporaryData {
  private var id = 0
  def getId = {
    val d = id
    id += 1
    d
  }
}

sealed abstract class DataType
case object FloatType extends DataType
case object DoubleType extends DataType

sealed abstract class DataDesc
case class ArrayDesc(dim:Int,sizes:Seq[Int],elem:DataDesc) extends DataDesc
case class MatrixDesc(m:Int,n:Int,typ:DataType) extends DataDesc
case class VectorDesc(n:Int,typ:DataType) extends DataDesc
case object DummyDesc extends DataDesc

sealed abstract class DataInit
case object RandomInit extends DataInit
case object ZeroInit extends DataInit
case object NoInit extends DataInit
