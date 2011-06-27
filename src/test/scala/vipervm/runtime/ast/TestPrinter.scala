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

import org.vipervm.runtime.ast._

class TestAstPrinter extends FunSuite {

  test("Test1") {
    val prog = AbsTerm(VarTerm(0,1))
    Printer.print(new Context, prog)
    println("\n")
  }

  test("Bad index") {
    val prog = AbsTerm(AbsTerm(VarTerm(0,1)))
    Printer.print(new Context, prog)
    println("\n")
  }

  test("Bad context size") {
    val error = try {
      val prog = AbsTerm(VarTerm(1,1))
      Printer.print(new Context, prog)
      false
    } catch {
      case _ => true
    }
    println("\n")
    assert(error)
  }

  test("Context size") {
    val prog = AbsTerm(AbsTerm(VarTerm(0,2)), "age")
    Printer.print(new Context, prog)
    println("\n")
  }

}
