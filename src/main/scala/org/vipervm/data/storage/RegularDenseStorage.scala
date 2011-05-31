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

package org.vipervm.data.storage

import org.vipervm.data.domains.RegularDenseDomain
import org.vipervm.data._
import org.vipervm.data.morphisms._

/**
 * Regular dense storage over a RegularDenseDomain
 *
 */
//TODO: support alignments
class RegularDenseStorage {

  /**
   * Perform convertion between a logical index and a physical index
   */
  def rawIndex(domain:RegularDenseDomain, index:Tuple):Expr = indexExpr(domain, index.elems)

  /**
   * Return the morphism performing the convertion between logical and physical index spaces
   */
  def morphism(domain:RegularDenseDomain):Morphism = {
    val dom = domain

    new Morphism {
      val domain = TupleType(dom.ndims, Seq(Int64Type))
      val codomain = Int64Type
      val f:PartialFunction[Expr,Expr] = {
        case Tuple(elems) => indexExpr(dom,elems)
      }
    }
  }

  protected def indexExpr(domain:RegularDenseDomain,index:Vector[Expr]):Expr = {
    val sizes = domain.dims.scanLeft(0L)((a,b) => a+(b._2-b._2+1))
    (index zip sizes).map(a => Mul(a._1,ValLong(a._2))).fold(ValLong(0))(Add(_,_))
  }
}
