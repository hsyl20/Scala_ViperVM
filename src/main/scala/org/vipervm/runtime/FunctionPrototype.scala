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

package org.vipervm.runtime

import org.vipervm.runtime.mm.config._
import org.vipervm.runtime.mm.{Data,MetaData,VVMType}

trait FunctionPrototype {
  /** Identifier of the function */
  val name:String

  /** Number of parameters */
  val arity:Int

  /** Compute result type if parameters are valid */
  def resultType(params:Seq[VVMType]):Option[VVMType]

  /** Compute from parameters the memory configuration required to compute result meta data */
  def metaConf(params:Seq[Data]): DataConfig

  /* Compute result meta data from parameters (with the configuration returned by metaConf) */
  def meta(params:Seq[Data]): MetaData
}
