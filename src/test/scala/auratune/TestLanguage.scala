import org.junit._
import fr.hsyl20.auratune._
import fr.hsyl20.auratune.Conversions._

class TestLanguage {
   @Test def language {

      val s = {
         'c := 'a + 'b
      }
      println(s)

      val s1 = new FloatMatrix
      val s2 = new FloatMatrix
      val d = new FloatMatrix
      val c = new Codelet("matrix_add", s1, s2, d)

      c.addStatement(s, Map('a -> s1, 'b -> s2, 'c -> d))

      println(c.cl)
   }
}
