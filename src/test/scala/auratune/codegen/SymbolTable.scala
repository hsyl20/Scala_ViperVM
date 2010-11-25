import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.auratune.codegen._

class SymbolTableSpec extends FlatSpec with ShouldMatchers {

   "A symbol table" should "be able to store elements" in {
      val s = SymbolTable.empty[String]
      val s2 = s declare ('a -> "pouet")
   }
}
