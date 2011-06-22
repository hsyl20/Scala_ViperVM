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
  def apply(ts:Term*) = Application(this, ts.toList)
}

case class DataTerm(data:Data) extends Term
case class KernelTerm(kernel:FunctionalKernel) extends Term
case class Application(f:Term, args:List[Term]) extends Term
