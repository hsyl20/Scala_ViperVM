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

class MatrixMultiplication() {
  val s = this.getClass.getResource("linearalgebra/multiplication.cl")
  if (s == null)
    throw new Exception("Unable to find kernel in library")
  val source = Source.fromURL(s)

  for (line <- source.getLines)
    println(line)
}
