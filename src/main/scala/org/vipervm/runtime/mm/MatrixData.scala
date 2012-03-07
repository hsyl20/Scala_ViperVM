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

case class MatrixType(elem:PrimitiveType) extends VVMType

case class MatrixMetaData(width:Long, height:Long) extends MetaData

sealed abstract class Major
case object ColumnMajor extends Major
case object RowMajor extends Major

abstract class MatrixRepr extends Repr

/** A matrix stored in contiguous memory */
case class DenseMatrixRepr(major:Major) extends MatrixRepr

/** A matrix stored using padding between rows */
case class StridedMatrixRepr(major:Major) extends MatrixRepr

/** A matrix stored using padding between rows and between elements */
case class DoubleStridedMatrixRepr(major:Major) extends MatrixRepr

/** A matrix stored in multiple other matrices */
case object CompositeMatrixRepr extends MatrixRepr


abstract class SparseMatrixRepr extends MatrixRepr
case object CompressedRowSparseMatrixRepr extends SparseMatrixRepr
case object CompressedColumnSparseMatrixRepr extends SparseMatrixRepr
case object BlockCompressedRowSparseMatrixRepr extends SparseMatrixRepr

/** Instance of a Matrix */
abstract class MatrixInstance extends DataInstance {
  val typ:MatrixType
  val meta:MatrixMetaData
  val repr:MatrixRepr
}

case class DenseMatrixInstance(typ:MatrixType,meta:MatrixMetaData,repr:DenseMatrixRepr,view:BufferView1D) extends MatrixInstance {
  def isAvailableIn(memory:MemoryNode) = Right(view.buffer.memory == memory)
}

case class StridedMatrixInstance(typ:MatrixType,meta:MatrixMetaData,repr:StridedMatrixRepr,view:BufferView2D,padding:Long) extends MatrixInstance {
  def isAvailableIn(memory:MemoryNode) = Right(view.buffer.memory == memory)
}

case class DoubleStridedMatrixInstance(typ:MatrixType,meta:MatrixMetaData,repr:DoubleStridedMatrixRepr,view:BufferView3D,cellPadding:Long,rowPadding:Long) extends MatrixInstance {
  def isAvailableIn(memory:MemoryNode) = Right(view.buffer.memory == memory)
}

case class CompositeMatrixInstance(typ:MatrixType,meta:MatrixMetaData,blocks:Seq[Seq[Matrix]]) extends MatrixInstance {

  val repr = CompositeMatrixRepr

  def isAvailableIn(memory:MemoryNode) = Left(blocks.flatten.map(_.data))
}
