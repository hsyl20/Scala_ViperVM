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

import fr.hsyl20.vipervm._

class ComponentsSpec extends FlatSpec with ShouldMatchers {

   "ExprComponents" should "work" in {
      val e = new ExprComponent {
         val in = new Port
         val in2 = new Port
         val in3 = new Port
         val out = new Port
         
         in * in2 + in3 --> out
      }
   }

   "MapComponents" should "work" in {
      val comp = new CompositeComponent {
         val e = new ExprComponent {
            val in = new Port
            val out = new Port
         
            in * in --> out
         }

         val m = new MapComponent(e.in, e.out)
      }
   }
}
