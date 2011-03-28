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

import fr.hsyl20.vipervm.compilers.ast._

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.PrintWriter

class ASTDslSpec extends FlatSpec with ShouldMatchers with TreeDSL  {

  import CODE._
  val printer = new TreePrinter(new PrintWriter(TreePrinter.ConsoleWriter))

  "A module" can "be declared" in {
      val tree = MODULE("test.module")()
      printer.print(tree)
  }

  "A module" can "contain val definition" in {
      val tree = MODULE("test.module")(
        VAL("a") === LIT(10)
      )
      printer.print(tree)
  }
}
