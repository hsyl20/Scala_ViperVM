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

package org.vipervm.runtime.interpreter

import org.vipervm.runtime.mm.{Data,VVMType}
import org.kiama.attribution._

sealed abstract class Term extends Attributable
case class TmApp(t1:Term,args:Seq[Term]) extends Term
case class TmId(id:String) extends Term
case class TmData(data:Data) extends Term
