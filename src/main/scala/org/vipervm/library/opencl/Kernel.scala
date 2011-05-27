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

package org.vipervm.library.opencl

import scala.io.Source

trait Kernel {
  protected def fromResource(path:String): Source = {
    val s = this.getClass.getResource(path)
    if (s == null)
      throw new Exception("Unable to find kernel in library (%s)".format(path))
    Source.fromURL(s)
  }
}
