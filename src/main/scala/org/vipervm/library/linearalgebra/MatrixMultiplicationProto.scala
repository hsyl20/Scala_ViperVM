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

package org.vipervm.library.linearalgebra

import org.vipervm.runtime.FunctionPrototype
import org.vipervm.runtime.mm.config._
import org.vipervm.runtime.mm.{Data,MatrixType,MetaData,MatrixMetaData,Matrix,VVMType}

object MatrixMultiplicationProto extends FunctionPrototype {
  val name = "*"
  val arity = 2
    
  def resultType(params:Seq[VVMType]):Option[VVMType] = params match {
    case Seq(MatrixType(a),MatrixType(b)) if a == b => Some(MatrixType(a))
    case _ => None
  }
  
  def metaConf(params:Seq[Data]):DataConfig = {
    (params(0) metaDataRequired) && (params(1) metaDataRequired)
  }

  def meta(params:Seq[Data]): MetaData = {
    val a = Matrix(params(0)).meta
    val b = Matrix(params(1)).meta
    MatrixMetaData(b.width,a.height)
  }
}
