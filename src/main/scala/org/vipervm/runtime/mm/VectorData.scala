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

import org.vipervm.platform._

case class VectorType(elem:PrimitiveType) extends VVMType

case class VectorMetaData(width:Long) extends MetaData

abstract class VectorRepr extends Repr


/** A vector stored in contiguous memory */
case object DenseVectorRepr extends VectorRepr

/** A vector stored using padding between elements */
case object StridedVectorRepr extends VectorRepr

/** A vector stored in multiple other vectors */
case object CompositeVectorRepr extends VectorRepr



/** Instance of a Vector */
abstract class VectorInstance extends DataInstance {
  val repr:VectorRepr
}

case class DenseVectorInstance(typ:VectorType,meta:VectorMetaData,view:BufferView1D) extends VectorInstance {
  val repr = DenseVectorRepr

  def isAvailableIn(memory:MemoryNode) = Right(view.buffer.memory == memory)
}

case class StridedVectorInstance(typ:VectorType,meta:VectorMetaData,view:BufferView2D,padding:Long) extends VectorInstance {
  val repr = StridedVectorRepr

  def isAvailableIn(memory:MemoryNode) = Right(view.buffer.memory == memory)
}

case class CompositeVectorInstance(typ:VectorType,meta:VectorMetaData,blocks:Seq[Vector]) extends VectorInstance {
  val repr = CompositeVectorRepr

  def isAvailableIn(memory:MemoryNode) = Left(blocks.map(_.data))
}
