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

package org.vipervm.runtime.mm.data

import java.nio.ByteOrder

import org.vipervm.platform._
import org.vipervm.runtime.mm._

abstract class PrimitiveType extends VVMType

case object FloatType extends PrimitiveType
case object DoubleType extends PrimitiveType
case object Int32Type extends PrimitiveType
case object Int64Type extends PrimitiveType

case object PrimitiveMetaData extends MetaData

case class PrimitiveRepr(endianness:ByteOrder) extends Repr

case class PrimitiveInstance(repr:PrimitiveRepr,view:BufferView1D) extends DataInstance[PrimitiveRepr]

object Primitive {

  /** Return the size in bytes of a primitive */
  def sizeOf(typ:PrimitiveType):Long = typ match {
    case FloatType => 4
    case DoubleType => 8
    case Int32Type => 4
    case Int64Type => 8
  }
}

abstract class PrimType[A](val typ:PrimitiveType) {
  def set(hostBuffer:HostBuffer,offset:Long,value:A):Unit
  def get(hostBuffer:HostBuffer,offset:Long):A
}
