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

import org.scalatest.FunSuite

import org.vipervm.library.linearalgebra._
import org.vipervm.dsl.linearalgebra._

class TestDslLinearAlgebra extends FunSuite {

  test("Printer") {
    val m = LowerTriangularMatrix[Num[Int]](100L)
    val x = m.dropColumn(2)
    val y = x.map(a => a*a)

    println(Printer.print(y))
  }
}


