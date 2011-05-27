/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.vipervm.codegen.opencl._

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
