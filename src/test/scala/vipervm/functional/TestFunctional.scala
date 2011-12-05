
import org.scalatest.FunSuite
import org.vipervm.functional._

class TestFunctional extends FunSuite {

  test("Print application") {
    val e = Reduce(Plus)
    val p = new Program("main" -> e)
    println(PrettyPrint.perform(p))
  }

  test("Print abstraction") {
    val e = Abstraction(x => Reduce(Plus)(x))
    val p = new Program("main" -> e)
    println(PrettyPrint.perform(p))
  }

  test("Unique new names") {
    val e1 = Reduce(Plus)
    val e = Abstraction(x => Reduce(Plus)(x))
    val p = new Program("a" -> e1, "main" -> e)
    println(PrettyPrint.perform(p))
  }

  test("Print graph") {
    val e1 = Abstraction(x => Reduce(Plus)(x))
    val e2 = Abstraction(x => Abstraction(y => Reduce(Plus)(ZipWith(Time)(x)(y))))
    val p = new Program("e1" -> e1, "e2" -> e2)
    println(PrettyPrint.perform(p))
  }

  test("CSE") {
    val e1 = Abstraction(x => Reduce(Plus)(x))
    val e2 = Abstraction(x => Abstraction(y => Reduce(Plus)(ZipWith(Var("e3"))(x)(y))))
    val e3 = Abstraction(x => Reduce(Plus)(x))
    val p = new Program("e1" -> e1, "e2" -> e2, "e3" -> e3)
    val p1 = CSE.perform(p)
    println(PrettyPrint.perform(p1))
  }
}
