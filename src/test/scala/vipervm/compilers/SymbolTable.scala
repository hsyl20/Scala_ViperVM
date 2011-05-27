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

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.vipervm.compilers._
import org.vipervm.compilers.ast._

class SymbolTableSpec extends FlatSpec with ShouldMatchers {

  "A symbol table" should "be able to store entities" in {
    val s = SymbolTable.empty

    s declare ("a" -> Def(Literal(Constant("dummy"))))
    s should (contain key "a")
  }

}
