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
import org.vipervm.runtime.Runtime

abstract class PrimitiveType(val size:Int) extends VVMType
case object FloatType extends PrimitiveType(4)
case object DoubleType extends PrimitiveType(8)
case object Int32Type extends PrimitiveType(4)
case object Int64Type extends PrimitiveType(8)

case object PrimitiveMetaData extends MetaData
case object PrimitiveRepr extends Repr
case class PrimitiveProperties(endianness:ByteOrder) extends ReprProperties
case class PrimitiveStorage(view:BufferView1D) extends Storage(view){
  def duplicate(views:Seq[BufferView]):Storage = views match {
    case Seq(x:BufferView1D) => PrimitiveStorage(x)
  }
}

case class PrimitiveInstance(typ:PrimitiveType,properties:PrimitiveProperties,storage:PrimitiveStorage) extends DataInstance {
  val repr = PrimitiveRepr
  val meta = PrimitiveMetaData
}


class Primitive[A](val data:Data,runtime:Runtime) extends DataWrapper {
}

abstract class PrimType[A](val typ:PrimitiveType) {
  val size = typ.size
  def set(hostBuffer:HostBuffer,offset:Long,value:A):Unit
  def get(hostBuffer:HostBuffer,offset:Long):A
}

object Primitives {

  def create[A](value:A)(implicit runtime:Runtime,p:PrimType[A]): Primitive[A] = {

    /* Create data */
    val data = runtime.createData

    /* Set type and meta data */
    val typ = p.typ
    val meta = PrimitiveMetaData
    data.typ = typ
    data.meta = meta

    val mem = runtime.platform.hostMemory
    val instance = allocate(p.typ,mem)

    /* Initialize instance */
    val buffer = instance.storage.view.buffer.asInstanceOf[HostBuffer]
    p.set(buffer, 0, value)

    /* Associate instance to the data */
    data.associate(instance)

    /* Return the data */
    new Primitive(data,runtime)
  }

  def allocate[A](memory:MemoryNode)(implicit p:PrimType[A]):PrimitiveInstance = {
    allocate(p.typ,memory)
  }

  def allocate(typ:PrimitiveType,memory:MemoryNode):PrimitiveInstance = {
    val prop = PrimitiveProperties(memory.endianness)
    val buffer = memory.allocate(typ.size)
    val view = BufferView1D(buffer, 0, typ.size)
    val storage = PrimitiveStorage(view)
    PrimitiveInstance(typ,prop,storage)
  }

}
