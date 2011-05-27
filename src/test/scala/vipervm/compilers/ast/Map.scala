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

import org.junit._
import org.scalatest.junit.JUnitSuite
import org.vipervm.compilers.ast._
import java.io.PrintWriter

class TestAstMap extends JUnitSuite with TreeDSL {
  @Test def astMap {
    import CODE._
    val printer = new TreePrinter(new PrintWriter(TreePrinter.ConsoleWriter))

    val tree = PACKAGE("test.map")(
      VAL("f") === FUNCTION(
        VAL("a") withType TYP("Float")  //Args
      )(
        TYP("Float")                    // Return type
      )(
        (ID("a") DOT ("*"))(LIT(2))  // Body
      ),

      VAL("x") === (ID("Matrix") DOT "loadFromFile")(LIT("in.dat")),

      VAL("y") === (ID("x") DOT "map")(ID("f")),

      (ID("y") DOT "saveToFile")(LIT("out.dat"))
    )

    printer.print(tree)
  }
}
