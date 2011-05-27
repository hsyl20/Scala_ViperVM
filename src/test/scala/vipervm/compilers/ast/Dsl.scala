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

import org.vipervm.compilers.ast._

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.PrintWriter

class ASTDslSpec extends FlatSpec with ShouldMatchers with TreeDSL  {

  import CODE._
  val printer = new TreePrinter(new PrintWriter(TreePrinter.ConsoleWriter))

  "A package" can "be declared" in {
      val tree = PACKAGE("test.module")()
      printer.print(tree)
  }

  "A package" can "contain val definition" in {
      val tree = PACKAGE("test.module")(
        VAL("a") === LIT(10)
      )
      printer.print(tree)
  }
}
