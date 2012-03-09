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

import org.vipervm.runtime.mm.Data

sealed abstract class Term
case class TmVar(index:Int,contextLength:Int) extends Term
case class TmAbs(varName:String,term:Term) extends Term
case class TmApp(t1:Term,args:Seq[Term]) extends Term

case class TmId(id:String) extends Term
case class TmData(data:Data) extends Term
