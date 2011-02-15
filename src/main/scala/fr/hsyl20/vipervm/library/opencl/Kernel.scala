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

package fr.hsyl20.vipervm.library.opencl

import scala.io.Source

trait Kernel {
  protected def fromResource(path:String): Source = {
    val s = this.getClass.getResource(path)
    if (s == null)
      throw new Exception("Unable to find kernel in library (%s)".format(path))
    Source.fromURL(s)
  }
}
