import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.auratune.dsl._

class SimpleDSLSpec extends FlatSpec with ShouldMatchers {

   "SimpleDSL" should "be able to use map" in {
      val m = new Matrix2D(TFloat, 1000, 2000)

      val n = m.map(a => a*a)

      val res = SimpleDSL compute (n) where (
         m -> 'm,
         n -> 'n)

      println(res)
   }
}
