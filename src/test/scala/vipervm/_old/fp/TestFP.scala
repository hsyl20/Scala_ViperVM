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

  test("Parser let") {
    tryParse("let a = true in false")
    tryParse("let a = true in (if true then false else true)")
  }

  test("Evaluation: R = A*B + A*C") {
    val matmul = TmKernel("matmul",2)
    val matadd = TmKernel("matadd", 2)
    val a = TmData(9000)
    val b = TmData(9001)
    val c = TmData(9002)
    val prog = TmApp(TmApp(matadd, TmApp(TmApp(matmul, a), b)), TmApp(TmApp(matmul, a), c))
    println(Term.eval(new Context,prog))
  }

  test("Evaluation: R = if true then A*B else A*C") {
    val matmul = TmKernel("matmul",2)
    val matadd = TmKernel("matadd", 2)
    val a = TmData(9000)
    val b = TmData(9001)
    val c = TmData(9002)
    val e = TmData(9003)
    val prog = TmIf(TmTrue, TmApp(TmApp(matmul, a), b), TmApp(TmApp(matmul, a), c))
    println(Term.eval(new Context,prog))
  }

}
