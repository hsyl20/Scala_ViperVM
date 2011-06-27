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

package org.vipervm.runtime.ast

import org.vipervm.runtime.Data
import org.vipervm.runtime.FunctionalKernel

sealed abstract class Term {
  def apply(ts:Term*) = (this /: ts.toList)(AppTerm(_,_))
}

/**
 * A variable with its de Bruijn index
 * ctxSize is used to check that context size is coherent
 */
case class VarTerm(idx:Int,ctxSize:Int) extends Term

/**
 * An abstraction with its content
 * name field can be used to store the variable name
 */
case class AbsTerm(body:Term, name:String = "") extends Term

/** An application */
case class AppTerm(left:Term, right:Term) extends Term

case class DataTerm(data:Data) extends Term
case class KernelTerm(kernel:FunctionalKernel) extends Term
