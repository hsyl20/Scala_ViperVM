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

import org.vipervm.parser._

class TestParser extends FunSuite {

  val displayResult = true

  private def parse(s:String) = {
    val res = Parser.parse(s)
    if (displayResult) println(res)
    assert(res.successful)
  }

  test("Function definition (body = ident)") {
    parse( "def test(m,n) = a" )
  }

  test("Function definition (body = method call without parameter)") {
    parse( "def test(m,n) = a.check" )
  }

  test("Function definition (body = method call with ident parameters)") {
    parse( "def test(m,n) = a.check(x,y)" )
  }

  test("Function definition (body = block containing ident)") {
    parse( """def test(m,n) = {
      a
    }""" )
  }

  test("Function definition (body = block containing a value definition)") {
    parse( """def test(m,n) = {
      val a = b
    }""" )
  }

  test("Function definition (body = block containing several value definitions)") {
    parse( """def test(m,n) = {
      val a = b;
      val c = d.apply(x,y,z);
    }""" )
  }

  test("Closure") {
    parse( "def test(m,n) = a.map(x => y) " )
  }

  test("Closure with complex body") {
    parse( """def test(m,n) = a.map(x => {
      y.remove(x.first, w.top(n))
    }) """ )
  }

  test("Nested closures") {
    parse( "def test(m,n) = a.map(x => y => x.add(y)) " )
  }

  test("Package definition") {
    parse( """package test.brol {
      def test(m,n) = a.map(x => y => x.add(y))
    }""" )
  }

  test("Import in body") {
    parse( """def test(x) = {
      import test.brol
    }""" )
  }

  test("Import in body with wildcard") {
    parse( """def test(x) = {
      import test.brol._
    }""" )
  }

  test("Import in complex body") {
    parse( """def test(x) = {
      import test.brol._;
      a.map(x=>y);
      import test.chmol;
      x.check(w)
    }""" )
  }

  test("Import in module") {
    parse( """package test.brol {
      import test.brol;
      def test(x) = a
    }""" )
  }

  test("Infix notation") {
    parse( "def test(n) = x add n")
  }
}
