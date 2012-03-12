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

case class Matrix(val data:Data) extends DataWrapper {
  def typOption:Option[MatrixType] = data.typ.map(_.asInstanceOf[MatrixType])
  def typ = typOption.get

  def metaOption:Option[MatrixMetaData] = data.meta.map(_.asInstanceOf[MatrixMetaData])
  def meta = metaOption.get
}

object Matrix {
  def create[A](dataManager:DataManager,width:Long,height:Long,major:Major = RowMajor)(f:(Long,Long)=>A)(implicit prim:PrimType[A]): Matrix = {
    
    /* Create data */
    val data = dataManager.create

    /* Set type and meta data */
    val typ = MatrixType(prim.typ)
    val meta = MatrixMetaData(width,height)
    dataManager.setType(data, typ)
    dataManager.setMetaData(data, meta)

    /* Create data instance */
    val repr = DenseMatrixRepr(major)
    val mem = dataManager.platform.hostMemory
    val instance = allocateDenseMatrix(typ,meta,repr,mem)

    /* Initialize instance */
    val view = instance.view
    for (y <- 0L until height; x <- 0L until width) {
      val index = x*4 + y * width * 4 + view.offset
      prim.set(view.buffer.asInstanceOf[HostBuffer], index, f(x,y))
    }

    /* Associate instance to the data */
    dataManager.associate(data,instance)

    /* Return the data */
    new Matrix(data)
  }

  def allocate(typ:MatrixType,meta:MatrixMetaData,repr:MatrixRepr,memory:MemoryNode):MatrixInstance = repr match {
    case r@DenseMatrixRepr(_) => allocateDenseMatrix(typ,meta,r,memory)
    case r@StridedMatrixRepr(_) => allocateStridedMatrix(typ,meta,r,memory)
    case _ => throw new Exception("Representation not allocatable")
  }

  def allocateDenseMatrix(typ:MatrixType,meta:MatrixMetaData,repr:DenseMatrixRepr,memory:MemoryNode):DenseMatrixInstance = {
    val elemSize = Primitive.sizeOf(typ.elem)
    val size = meta.width * meta.height * elemSize
    val buffer = memory.allocate(size)
    val view = new BufferView1D(buffer, 0, size)
    val instance = DenseMatrixInstance(typ,meta,repr,view)
    instance
  }

  def allocateStridedMatrix(typ:MatrixType,meta:MatrixMetaData,repr:StridedMatrixRepr,memory:MemoryNode,padding:Long=0L):StridedMatrixInstance = {
    val elemSize = Primitive.sizeOf(typ.elem)
    val size = (meta.width * elemSize + padding) * meta.height
    val buffer = memory.allocate(size)
    val view = new BufferView2D(buffer, 0, meta.width * elemSize, meta.height, padding)
    val instance = StridedMatrixInstance(typ,meta,repr,view,padding)
    instance
  }

}
