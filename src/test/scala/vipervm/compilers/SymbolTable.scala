/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.vipervm.compilers._
import fr.hsyl20.vipervm.compilers.ast._

class SymbolTableSpec extends FlatSpec with ShouldMatchers {

   "A symbol table" should "be able to store elements" in {
      val s = SymbolTable.empty
      val s2 = s declare ("a" -> Def(Literal(Constant("pouet"))))
   }
}
