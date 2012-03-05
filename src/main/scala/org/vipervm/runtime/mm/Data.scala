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

package org.vipervm.runtime.mm

import org.vipervm.platform.MemoryNode

trait Data {

  type T <: VVMType
  type M <: MetaData
  type R <: Repr
  type I <: DataInstance[R]

  /** Type of the data */
  def typ:T

  /** Meta data associated to the data type */
  def metadata:M

  def allocate(memory:MemoryNode,repr:R):Either[AllocationFailure,I]
}

/** Instance of a data */
trait DataInstance[+R<:Repr] {
  /** Selected way to represent the data */
  def repr:R
}

sealed abstract class AllocationFailure
case object OutOfMemoryFailure extends AllocationFailure
case object DataRepresentationNotSupported extends AllocationFailure
