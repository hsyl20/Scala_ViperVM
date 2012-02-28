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

package org.vipervm.platform

import scala.util.continuations._

object Barrier {
  def barrier[T](event:Event):Unit @cps[FutureEvent[T]]= {
    shift { k: (Unit=>FutureEvent[T]) =>
      event.fold(k())
    }
  }

  def sequence[A,C](body: => A@cpsParam[A,C]):C = reset { body }
}
