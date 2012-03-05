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

import org.vipervm.platform._
import org.vipervm.runtime.mm._
import org.vipervm.runtime.mm.data._

abstract class MatrixData extends Data {
  type T = MatrixType
  type M = MatrixMetaData
  type R = MatrixRepr
  type I = MatrixInstance
}

case class MatrixType(elem:PrimitiveType) extends VVMType

case class MatrixMetaData(width:Long, height:Long) extends MetaData

sealed abstract class Major
case object ColumnMajor extends Major
case object RowMajor extends Major

abstract class MatrixRepr extends Repr

/** A matrix stored in contiguous memory */
case class DenseMatrixRepr(elem:PrimitiveRepr,major:Major) extends MatrixRepr

/** A matrix stored using padding between rows */
case class StridedMatrixRepr(elem:PrimitiveRepr,major:Major,padding:Long) extends MatrixRepr

/** A matrix stored using padding between rows and between elements */
case class DoubleStridedMatrixRepr(elem:PrimitiveRepr,major:Major,cellPadding:Long,rowPadding:Long) extends MatrixRepr

/** A matrix stored in multiple other matrices */
case class CompositeMatrixRepr(blocks:Seq[Seq[Matrix]]) extends MatrixRepr


abstract class SparseMatrixRepr extends MatrixRepr
case object CompressedRowSparseMatrixRepr extends SparseMatrixRepr
case object CompressedColumnSparseMatrixRepr extends SparseMatrixRepr
case object BlockCompressedRowSparseMatrixRepr extends SparseMatrixRepr

/** Instance of a Matrix */
abstract class MatrixInstance(repr:MatrixRepr) extends DataInstance[MatrixRepr]

case class DenseMatrixInstance(repr:DenseMatrixRepr,view:BufferView1D) extends MatrixInstance(repr) {
  def isAvailableIn(memory:MemoryNode) = Right(view.buffer.memory == memory)
}

case class StridedMatrixInstance(repr:StridedMatrixRepr,view:BufferView2D) extends MatrixInstance(repr) {
  def isAvailableIn(memory:MemoryNode) = Right(view.buffer.memory == memory)
}

case class CompositeMatrixInstance(repr:CompositeMatrixRepr) extends MatrixInstance(repr) {
  def isAvailableIn(memory:MemoryNode) = Left(repr.blocks.flatten)
}
