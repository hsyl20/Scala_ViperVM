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

case class VectorType(elem:PrimitiveType) extends VVMType
case class VectorMetaData(width:Long) extends MetaData
abstract class VectorRepr extends Repr

/** A vector stored in contiguous memory */
case object DenseVectorRepr extends VectorRepr
case class DenseVectorProperties(endianness:ByteOrder) extends ReprProperties
case class DenseVectorStorage(view:BufferView1D) extends Storage(view)
case class DenseVectorInstance(typ:VectorType,meta:VectorMetaData,properties:DenseVectorProperties,storage:DenseVectorStorage) extends DataInstance(typ,meta,DenseVectorRepr,properties,storage)

/** A vector stored using padding between elements */
case object StridedVectorRepr extends VectorRepr
case class StridedVectorProperties(endianness:ByteOrder,padding:Long) extends ReprProperties
case class StridedVectorStorage(view:BufferView2D) extends Storage(view)
case class StridedVectorInstance(typ:VectorType,meta:VectorMetaData,properties:StridedVectorProperties,storage:StridedVectorStorage) extends DataInstance(typ,meta,StridedVectorRepr,properties,storage)


case class Vector(val data:Data) extends DataWrapper {
  def typOption:Option[VectorType] = data.typ.map(_.asInstanceOf[VectorType])
  def typ = typOption.get

  def metaOption:Option[VectorMetaData] = data.meta.map(_.asInstanceOf[VectorMetaData])
  def meta = metaOption.get
}

object Vector {
  def create[A](width:Long,f:(Long)=>A)(implicit dataManager:DataManager,prim:PrimType[A]): Vector = {
    
    /* Create data */
    val data = dataManager.create

    /* Set type and meta data */
    val typ = VectorType(prim.typ)
    val meta = VectorMetaData(width)
    dataManager.setType(data, typ)
    dataManager.setMetaData(data, meta)

    val mem = dataManager.platform.hostMemory
    val instance = allocateDenseVector(typ,meta,mem)

    /* Initialize instance */
    val view = instance.storage.view
    val buffer = view.buffer.asInstanceOf[HostBuffer]
    for (x <- 0L until width) {
      val index = x*4 + view.offset
      prim.set(buffer, index, f(x))
    }

    /* Associate instance to the data */
    dataManager.associate(data,instance)

    /* Return the data */
    new Vector(data)
  }

  def allocateDenseVector(typ:VectorType,meta:VectorMetaData,memory:MemoryNode):DenseVectorInstance = {
    val prop = DenseVectorProperties(memory.endianness)
    val elemSize = typ.elem.size
    val size = meta.width * elemSize
    val buffer = memory.allocate(size)
    val view = new BufferView1D(buffer, 0, size)
    val storage = DenseVectorStorage(view)
    DenseVectorInstance(typ,meta,prop,storage)
  }

  def allocateStridedVector(typ:VectorType,meta:VectorMetaData,memory:MemoryNode,padding:Long=0L):StridedVectorInstance = {
    val prop = StridedVectorProperties(memory.endianness,padding)
    val elemSize = typ.elem.size
    val size = meta.width * (elemSize + padding)
    val buffer = memory.allocate(size)
    val view = new BufferView2D(buffer, 0, elemSize, meta.width, padding)
    val storage = StridedVectorStorage(view)
    StridedVectorInstance(typ,meta,prop,storage)
  }
}
