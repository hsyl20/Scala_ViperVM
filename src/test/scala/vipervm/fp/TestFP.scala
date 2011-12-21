import org.scalatest.FunSuite
import org.vipervm.fp._

class TestFP extends FunSuite {

  def tryParse(s:String) = assert(Parser.parse(s).successful)

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
}
