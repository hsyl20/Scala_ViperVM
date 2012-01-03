import org.scalatest.FunSuite
import org.vipervm.fp._

class TestHPC extends FunSuite {

  val dtrsm = Cod("dtrsm", 2)
  val dpotf2 = Cod("dpotf2", 1)
  val dsyrk = Cod("dsyrk", 2)
  val m1 = Data(10000)
  val m2 = Data(11000)

  test("Codelet instances") {
    val prog = App(App(dtrsm, m1), m2)
    val res = FpTerm.eval(prog)
    println(res)
  }

  test("Application") {
    val prog = App(App(dtrsm, m1), App(dpotf2, m2))
    val res = FpTerm.eval(prog)
    println(res)
  }

}
