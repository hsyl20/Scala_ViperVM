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

package org.vipervm.data.domains

/**
 * A regular dense domain
 *
 * @param ndims Number of dimensions
 * @param dims Ranges of each dimension
 */
class RegularDenseDomain(val ndims:Int, val dims:Vector[(Long,Long)]) extends Domain {
  assert(ndims == dims.length)

  def dim(index:Int): (Long,Long) = dims(index)

  /**
    * Shift a domain by a vector
    */
  def shift(v:Vector[Long]): RegularDenseDomain = {
    assert(v.size == ndims)
    val newdims = for ((d,vi) <- dims zip v) yield (d._1+vi, d._2+vi)
    new RegularDenseDomain(ndims, newdims)
  }

  /**
    * Shift a domain by a vector
    */
  def @@(v:Vector[Long]) = shift(v)
}
