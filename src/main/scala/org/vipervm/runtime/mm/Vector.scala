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

case class Vector(val data:Data) extends DataWrapper {
  def typOption:Option[VectorType] = data.typ.map(_.asInstanceOf[VectorType])
  def typ = typOption.get

  def metaOption:Option[VectorMetaData] = data.meta.map(_.asInstanceOf[VectorMetaData])
  def meta = metaOption.get
}

object Vector {
  def create[A](dataManager:DataManager,width:Long,f:(Long)=>A)(implicit prim:PrimType[A]): Vector = {
    
    /* Create data */
    val data = dataManager.create

    val typ = VectorType(prim.typ)
    val meta = VectorMetaData(width)
    dataManager.setType(data, typ)
    dataManager.setMetaData(data, meta)

    /* Create instance */
    val repr = DenseVectorRepr
    val mem = dataManager.platform.hostMemory
    val instance = allocateDenseVector(typ,meta,mem)

    /* Initialize instance */
    val view = instance.view
    for (x <- 0L until width) {
      val index = x*4 + view.offset
      prim.set(view.buffer.asInstanceOf[HostBuffer], index, f(x))
    }

    /* Associate instance to the data */
    dataManager.associate(data,instance)

    /* Return the data */
    new Vector(data)
  }

  def allocateDenseVector(typ:VectorType,meta:VectorMetaData,memory:MemoryNode):DenseVectorInstance = {
    val elemSize = Primitive.sizeOf(typ.elem)
    val size = meta.width * elemSize
    val buffer = memory.allocate(size)
    val view = new BufferView1D(buffer, 0, size)
    val instance = DenseVectorInstance(typ,meta,view)
    instance
  }

  def allocateStridedVector(typ:VectorType,meta:VectorMetaData,memory:MemoryNode,padding:Long=0L):StridedVectorInstance = {
    val elemSize = Primitive.sizeOf(typ.elem)
    val size = meta.width * (elemSize + padding)
    val buffer = memory.allocate(size)
    val view = new BufferView2D(buffer, 0, elemSize, meta.width, padding)
    val instance = StridedVectorInstance(typ,meta,view,padding)
    instance
  }
}
