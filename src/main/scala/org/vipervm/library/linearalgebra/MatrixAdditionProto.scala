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
import org.vipervm.runtime.mm.{Data,MatrixType,MetaData,VVMType}

object MatrixAdditionProto extends FunctionPrototype {
  val name = "+"
    
  def typ(params:Seq[VVMType]):Option[VVMType] = params match {
    case Seq(MatrixType(a),MatrixType(b)) if a == b => Some(MatrixType(a))
    case _ => None
  }
  
  def metaConf(params:Seq[Data]):DataConfig = {
    (params(0) metaDataRequired) || (params(1) metaDataRequired)
  }

  def meta(params:Seq[Data]): MetaData = {
    /* One of the two params must have its meta data defined according to
     the metaConf above. Parameters and result have the same meta data */
    params(0).meta.getOrElse(params(1).meta.get)
  }
}
