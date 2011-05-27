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

package org.vipervm.data

sealed abstract class PrimitiveType
case object DoublePrimitive extends PrimitiveType
case object FloatPrimitive  extends PrimitiveType
case object Int32Primitive  extends PrimitiveType
case object Int64Primitive  extends PrimitiveType
case object Int16Primitive  extends PrimitiveType
case object BytePrimitive   extends PrimitiveType
case class  StructurePrimitive(fields:Seq[PrimitiveType]) extends PrimitiveType
