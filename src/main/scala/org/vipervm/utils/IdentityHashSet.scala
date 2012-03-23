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

package org.vipervm.utils

import scala.collection.mutable.{Set,Map}

class IdentityHashSet[A] extends Set[A] {

  private val elems:Map[Int,A] = Map.empty

  override def contains(elem:A):Boolean = elems.get(System.identityHashCode(elem)).isDefined

  override def iterator = elems.values.iterator

  override def +=(elem:A) = {
    elems += System.identityHashCode(elem) -> elem
    this
  }

  override def -=(elem:A) = {
    elems -= System.identityHashCode(elem)
    this
  }

  override def empty:IdentityHashSet[A] = new IdentityHashSet[A]
}

object IdentityHashSet {
  def empty[A] = new IdentityHashSet[A]
}
