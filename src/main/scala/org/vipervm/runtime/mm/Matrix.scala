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

case class MatrixType(elem:PrimitiveType) extends VVMType
case class MatrixMetaData(width:Long, height:Long) extends MetaData
abstract class MatrixRepr extends Repr

sealed abstract class Major
case object ColumnMajor extends Major
case object RowMajor extends Major

/** A vector stored in contiguous memory */
case object DenseMatrixRepr extends MatrixRepr
case class DenseMatrixProperties(major:Major,endianness:ByteOrder) extends ReprProperties
case class DenseMatrixStorage(view:BufferView1D) extends Storage(view)
case class DenseMatrixInstance(typ:MatrixType,meta:MatrixMetaData,properties:DenseMatrixProperties,storage:DenseMatrixStorage) extends DataInstance(typ,meta,DenseMatrixRepr,properties,storage)

/** A matrix stored using padding between rows */
case object StridedMatrixRepr extends MatrixRepr
case class StridedMatrixProperties(major:Major,endianness:ByteOrder,rowPadding:Long) extends ReprProperties
case class StridedMatrixStorage(view:BufferView2D) extends Storage(view)
case class StridedMatrixInstance(typ:MatrixType,meta:MatrixMetaData,properties:StridedMatrixProperties,storage:StridedMatrixStorage) extends DataInstance(typ,meta,StridedMatrixRepr,properties,storage)

/** A matrix stored using padding between rows and between elements */
case object DoubleStridedMatrixRepr extends MatrixRepr
case class DoubleStridedMatrixProperties(major:Major,endianness:ByteOrder,rowPadding:Long,cellPadding:Long) extends ReprProperties
case class DoubleStridedMatrixStorage(view:BufferView3D) extends Storage(view)
case class DoubleStridedMatrixInstance(typ:MatrixType,meta:MatrixMetaData,properties:DoubleStridedMatrixProperties,storage:DoubleStridedMatrixStorage) extends DataInstance(typ,meta,DoubleStridedMatrixRepr,properties,storage)


case class Matrix(val data:Data) extends DataWrapper {
  def typOption:Option[MatrixType] = data.typ.map(_.asInstanceOf[MatrixType])
  def typ = typOption.get

  def metaOption:Option[MatrixMetaData] = data.meta.map(_.asInstanceOf[MatrixMetaData])
  def meta = metaOption.get
}

object Matrix {
  def create[A](width:Long,height:Long,major:Major = RowMajor)(f:(Long,Long)=>A)(implicit runtime:Runtime,prim:PrimType[A]): Matrix = {
    
    /* Create data */
    val data = runtime.createData

    /* Set type and meta data */
    val typ = MatrixType(prim.typ)
    val meta = MatrixMetaData(width,height)
    data.typ = typ
    data.meta = meta

    val mem = runtime.platform.hostMemory
    val instance = allocateDenseMatrix(typ,meta,mem,major)

    /* Initialize instance */
    val view = instance.storage.view
    val buffer = view.buffer.asInstanceOf[HostBuffer]
    major match {
      case RowMajor => for (y <- 0L until height; x <- 0L until width) {
        val index = x * prim.size + y * width * prim.size + view.offset
        prim.set(buffer, index, f(x,y))
      }
      case ColumnMajor => for (x <- 0L until width; y <- 0L until height) {
        val index = y * prim.size + x * height * prim.size + view.offset
        prim.set(buffer, index, f(x,y))
      }
    }

    /* Associate instance to the data */
    data.associate(instance)

    /* Return the data */
    new Matrix(data)
  }

  def allocateDenseMatrix(typ:MatrixType,meta:MatrixMetaData,memory:MemoryNode,major:Major=RowMajor):DenseMatrixInstance = {
    val prop = DenseMatrixProperties(major,memory.endianness)
    val elemSize = typ.elem.size
    val size = meta.width * meta.height * elemSize
    val buffer = memory.allocate(size)
    val view = new BufferView1D(buffer, 0, size)
    val storage = DenseMatrixStorage(view)
    DenseMatrixInstance(typ,meta,prop,storage)
  }

  def allocateStridedMatrix(typ:MatrixType,meta:MatrixMetaData,memory:MemoryNode,major:Major=RowMajor,rowPadding:Long=0L):StridedMatrixInstance = {
    val prop = StridedMatrixProperties(major,memory.endianness,rowPadding)
    val elemSize = typ.elem.size
    val size = (meta.width * elemSize + rowPadding) * meta.height
    val buffer = memory.allocate(size)
    val view = new BufferView2D(buffer, 0, meta.width * elemSize, meta.height, rowPadding)
    val storage = StridedMatrixStorage(view)
    val instance = StridedMatrixInstance(typ,meta,prop,storage)
    instance
  }

}
