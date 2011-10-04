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

sealed abstract class Data
case class InitialData(name:String) extends Data
case class FilteredData(source:Data, filter:Filter) extends Data
case class DataSelect(src:FilteredData, id:Int*) extends Data
case class TemporaryData(id:Int) extends Data {
  def this() = this(TemporaryData.getId)
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
case class MatrixDesc(m:Int,n:Int,typ:DataType) extends DataDesc
case class VectorDesc(n:Int,typ:DataType) extends DataDesc
