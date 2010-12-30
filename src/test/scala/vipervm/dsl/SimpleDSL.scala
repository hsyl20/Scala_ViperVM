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

import fr.hsyl20.vipervm.dsl._

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
