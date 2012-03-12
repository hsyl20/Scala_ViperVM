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

class Primitive[A](val data:Data,dataManager:DataManager) extends DataWrapper {
}

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
  val size:Long
  def set(hostBuffer:HostBuffer,offset:Long,value:A):Unit
  def get(hostBuffer:HostBuffer,offset:Long):A
}

object Primitives {

  def create[A](dataManager:DataManager)(value:A)(implicit p:PrimType[A]): Primitive[A] = {

    /* Create data */
    val data = dataManager.create

    /* Set type and meta data */
    val typ = p.typ
    val meta = PrimitiveMetaData
    dataManager.setType(data, typ)
    dataManager.setMetaData(data, meta)

    /* Create data instance */
    val repr = PrimitiveRepr
    val mem = dataManager.platform.hostMemory
    val instance = allocate[A](mem)

    /* Initialize instance */
    val view = instance.view
    p.set(view.buffer.asInstanceOf[HostBuffer], 0, value)

    /* Associate instance to the data */
    dataManager.associate(data,instance)

    /* Return the data */
    new Primitive(data,dataManager)
  }


  def allocate[A](memory:MemoryNode,order:ByteOrder = ByteOrder.nativeOrder)(implicit p:PrimType[A]):PrimitiveInstance = {
    val size = p.size
    val typ = p.typ
    val buffer = memory.allocate(size)
    val view = new BufferView1D(buffer, 0, size)
    val instance = PrimitiveInstance(typ,view, order)
    instance
  }
}
