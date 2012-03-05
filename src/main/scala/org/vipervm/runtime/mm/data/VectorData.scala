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


package org.vipervm.runtime.mm.data.vector

import org.vipervm.platform._
import org.vipervm.runtime.mm._
import org.vipervm.runtime.mm.data.primitives._
import org.vipervm.runtime.mm.data._

abstract class VectorData extends Data {
  type T = VectorType
  type M = VectorMetaData
  type R = VectorRepr
  type I = VectorInstance
}

case class VectorType(elem:PrimitiveType) extends VVMType

case class VectorMetaData(width:Long) extends MetaData

abstract class VectorRepr extends Repr


/** A vector stored in contiguous memory */
case class DenseVectorRepr(elem:PrimitiveRepr) extends VectorRepr

/** A vector stored using padding between elements */
case class StridedVectorRepr(elem:PrimitiveRepr,padding:Long) extends VectorRepr

/** A vector stored in multiple other vectors */
case class CompositeVectorRepr(blocks:Seq[Vector]) extends VectorRepr



/** Instance of a Vector */
abstract class VectorInstance(repr:VectorRepr) extends DataInstance[VectorRepr]

case class DenseVectorInstance(repr:DenseVectorRepr,view:BufferView1D) extends VectorInstance(repr)

case class StridedVectorInstance(repr:StridedVectorRepr,view:BufferView2D) extends VectorInstance(repr)

case class CompositeVectorInstance(repr:CompositeVectorRepr) extends VectorInstance(repr)
