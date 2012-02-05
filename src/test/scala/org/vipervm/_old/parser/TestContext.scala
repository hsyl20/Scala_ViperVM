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
import org.scalatest.matchers.ShouldMatchers

import org.vipervm.parser._

class TestContext extends FunSuite with ShouldMatchers {

  test("Get on empty context throws an exception") {
    evaluating { (new Context).get("brol.troll") } should produce [InvalidPathException]
  }

  test("Invalid path triggers exception") {
    val c = Context.empty + ("brol" -> (Context.empty + ("troll" -> Context.empty)))
    evaluating { c.get("brol.troll.pouet.grol") } should produce [InvalidPathException]
  }

  test("Get inner context with valid path") {
    val c = Context.empty + ("brol" -> (Context.empty + ("troll" -> Context.empty)))
    c.get("brol.troll")
  }

}

