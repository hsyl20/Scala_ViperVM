import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.auratune.codegen.opencl._

class MapSpec extends FlatSpec with ShouldMatchers {

  "A map" should "produce valid kernel" in {
      val f = CFunction(CFloat, Variable(CFloat)) { case List(a) =>
         a * a
      }

      val src = Variable(CFloat*)
      val dest = Variable(CFloat*)

      val code = CMap(f, src, dest, 100)
      println(code)
  }

}
