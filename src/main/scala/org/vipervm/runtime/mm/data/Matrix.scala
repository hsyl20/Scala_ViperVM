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

package org.vipervm.runtime.mm.data.matrix

import java.nio.ByteOrder

import org.vipervm.platform._
import org.vipervm.runtime.mm._
import org.vipervm.runtime.mm.data._
import org.vipervm.runtime.mm.data.vector._

class Matrix(val typ:MatrixType, val metadata:MatrixMetaData) extends MatrixData {

  val (width,height) = (metadata.width,metadata.height)
  protected val elemSize = Primitive.sizeOf(typ.elem)
  
  def allocate(memory:MemoryNode,repr:MatrixRepr):Either[AllocationFailure,MatrixInstance] = {
    repr match {
      case r@DenseMatrixRepr(_,_) => allocateDenseMatrix(memory,r)
      case r@StridedMatrixRepr(_,_,_) => allocateStridedMatrix(memory,r)
      case _ => Left(DataRepresentationNotSupported)
    }
  }

  protected def allocateDenseMatrix(memory:MemoryNode,repr:DenseMatrixRepr):Either[AllocationFailure,DenseMatrixInstance] = {
    val size = width * height * elemSize
    val buffer = memory.allocate(size)
    val view = new BufferView1D(buffer, 0, size)
    val instance = DenseMatrixInstance(repr, view)
    Right(instance)
  }

  protected def allocateStridedMatrix(memory:MemoryNode,repr:StridedMatrixRepr):Either[AllocationFailure,StridedMatrixInstance] = {
    val size = (width * elemSize + repr.padding) * height
    val buffer = memory.allocate(size)
    val view = new BufferView2D(buffer, 0, width * elemSize, height, repr.padding)
    val instance = StridedMatrixInstance(repr, view)
    Right(instance)
  }

}

object Matrix {
  def create[A](dataManager:DataManager,width:Long,height:Long,f:(Long,Long)=>A,major:Major = RowMajor)(implicit prim:PrimType[A]): Matrix = {
    
    /* Create data */
    val typ = MatrixType(prim.typ)
    val metadata = MatrixMetaData(width,height)
    val data = new Matrix(typ,metadata)

    dataManager.register(data)

    /* Create instance */
    val repr = DenseMatrixRepr(PrimitiveRepr(ByteOrder.nativeOrder),major)
    val mem = dataManager.platform.hostMemory
    val instance = data.allocateDenseMatrix(mem,repr) match {
      case Left(err) => throw new Exception(err.toString)
      case Right(inst) => inst
    }

    /* Initialize instance */
    val view = instance.view
    for (y <- 0L until height; x <- 0L until width) {
      val index = x*4 + y * width * 4 + view.offset
      prim.set(view.buffer.asInstanceOf[HostBuffer], index, f(x,y))
    }

    /* Associate instance to the data */
    dataManager.associate(instance,data)

    /* Return the data */
    data
  }

}
