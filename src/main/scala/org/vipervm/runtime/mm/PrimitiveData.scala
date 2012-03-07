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

package org.vipervm.runtime.mm

import java.nio.ByteOrder

import org.vipervm.platform._

abstract class PrimitiveType extends VVMType
case object FloatType extends PrimitiveType
case object DoubleType extends PrimitiveType
case object Int32Type extends PrimitiveType
case object Int64Type extends PrimitiveType

case object PrimitiveMetaData extends MetaData
case object PrimitiveRepr extends Repr

case class PrimitiveInstance(typ:PrimitiveType,view:BufferView1D,endianness:ByteOrder) extends DataInstance {
  val meta = PrimitiveMetaData
  val repr = PrimitiveRepr

  def isAvailableIn(memory:MemoryNode) = Right(view.buffer.memory == memory)
}


