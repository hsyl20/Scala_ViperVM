/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.runtime

import fr.hsyl20.vipervm.runtime.AccessMode._
import scala.collection._

/** This represents an immutable data.
 *
 * Every buffer must contain the same data.
 * Data can be available or unavailable on each memory node
 */
abstract class ImmutableBufferSet extends BufferSet {
  import BufferState._

  override def notifyBeforeAccess(buffer:Buffer, mode:AccessMode): Unit = mode match {
    case ReadWrite | WriteOnly => throw new Exception("Trying to access immutable data in write mode")
    case ReadOnly => ()
  }
}

