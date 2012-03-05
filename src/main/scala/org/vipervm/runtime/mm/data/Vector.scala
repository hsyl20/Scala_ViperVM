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
import org.vipervm.runtime.mm.data._
import org.vipervm.runtime.mm.data.vector._

class Vector(val typ:VectorType, val metadata:VectorMetaData) extends VectorData {

  val width = metadata.width
  protected val elemSize = Primitive.sizeOf(typ.elem)

  def allocate(memory:MemoryNode,repr:VectorRepr):Either[AllocationFailure,VectorInstance] = {
    repr match {
      case r@DenseVectorRepr(_) => allocateDenseVector(memory,r)
      case r@StridedVectorRepr(_,_) => allocateStridedVector(memory,r)
      case _ => Left(DataRepresentationNotSupported)
    }
  }

  protected def allocateDenseVector(memory:MemoryNode,repr:DenseVectorRepr):Either[AllocationFailure,DenseVectorInstance] = {
    val size = width * elemSize
    val buffer = memory.allocate(size)
    val view = new BufferView1D(buffer, 0, size)
    val instance = DenseVectorInstance(repr, view)
    Right(instance)
  }

  protected def allocateStridedVector(memory:MemoryNode,repr:StridedVectorRepr):Either[AllocationFailure,StridedVectorInstance] = {
    val size = width * (elemSize + repr.padding)
    val buffer = memory.allocate(size)
    val view = new BufferView2D(buffer, 0, elemSize, width, repr.padding)
    val instance = StridedVectorInstance(repr, view)
    Right(instance)
  }
}

object Vector {
  def create[A](dataManager:DataManager,width:Long,f:(Long)=>A)(implicit prim:PrimType[A]): Vector = {
    
    /* Create data */
    val typ = VectorType(prim.typ)
    val metadata = VectorMetaData(width)
    val data = new Vector(typ,metadata)

    dataManager.register(data)

    /* Create instance */
    val repr = DenseVectorRepr(PrimitiveRepr(ByteOrder.nativeOrder))
    val mem = dataManager.platform.hostMemory
    val instance = data.allocateDenseVector(mem,repr) match {
      case Left(err) => throw new Exception(err.toString)
      case Right(inst) => inst
    }

    /* Initialize instance */
    val view = instance.view
    for (x <- 0L until width) {
      val index = x*4 + view.offset
      prim.set(view.buffer.asInstanceOf[HostBuffer], index, f(x))
    }

    /* Associate instance to the data */
    dataManager.associate(instance,data)

    /* Return the data */
    data
  }

}
