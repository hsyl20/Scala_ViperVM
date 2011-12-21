import org.scalatest.FunSuite
import org.vipervm.fp._

class TestFP extends FunSuite {

  def tryParse(s:String) = {
    val r = Parser.parse(s)
    println(r)
    assert(r.successful)
  }

  test("Parser Bool Untyped") {
    tryParse("true")
    tryParse("false")
    tryParse("if true then false else true")
    tryParse("if false then false else true")
    tryParse("(true)")
    tryParse("(false)")
    tryParse("(if true then true else true)")
    tryParse("if true then (if true then false else false) else true")
  }

  test("Parser Bool Nat Untyped") {
    tryParse("0")
    tryParse("pred (succ 0)")
    tryParse("isZero (succ 0)")
    tryParse("isZero (succ (succ 0))")
  }
}
