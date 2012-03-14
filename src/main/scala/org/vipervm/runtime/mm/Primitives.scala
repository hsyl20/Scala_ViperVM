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

abstract class PrimitiveType(val size:Int) extends VVMType
case object FloatType extends PrimitiveType(4)
case object DoubleType extends PrimitiveType(8)
case object Int32Type extends PrimitiveType(4)
case object Int64Type extends PrimitiveType(8)

case object PrimitiveMetaData extends MetaData
case object PrimitiveRepr extends Repr
case class PrimitiveProperties(endianness:ByteOrder) extends ReprProperties
case class PrimitiveStorage(view:BufferView1D) extends Storage(view)
case class PrimitiveInstance(typ:PrimitiveType,properties:PrimitiveProperties,storage:PrimitiveStorage) extends DataInstance(typ,PrimitiveMetaData,PrimitiveRepr,properties,storage)


class Primitive[A](val data:Data,dataManager:DataManager) extends DataWrapper {
}

abstract class PrimType[A](val typ:PrimitiveType) {
  val size = typ.size
  def set(hostBuffer:HostBuffer,offset:Long,value:A):Unit
  def get(hostBuffer:HostBuffer,offset:Long):A
}

object Primitives {

  def create[A](value:A)(implicit dataManager:DataManager,p:PrimType[A]): Primitive[A] = {

    /* Create data */
    val data = dataManager.create

    /* Set type and meta data */
    val typ = p.typ
    val meta = PrimitiveMetaData
    dataManager.setType(data, typ)
    dataManager.setMetaData(data, meta)

    val mem = dataManager.platform.hostMemory
    val instance = allocate(p.typ,mem)

    /* Initialize instance */
    val buffer = instance.storage.view.buffer.asInstanceOf[HostBuffer]
    p.set(buffer, 0, value)

    /* Associate instance to the data */
    dataManager.associate(data,instance)

    /* Return the data */
    new Primitive(data,dataManager)
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
