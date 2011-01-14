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
/*
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import fr.hsyl20.vipervm.runtime.DefaultRuntime
import fr.hsyl20.vipervm.dsl.linearalgebra._
import fr.hsyl20.vipervm.data._

class DSLSpec extends FlatSpec with ShouldMatchers {

  "A DSL" should "work as expected" in {

      implicit val runtime = new DefaultRuntime
      implicit val engine = new LinearAlgebraEngine

      val m = Matrix.loadFromStream[TDouble](getClass.getResourceAsStream("matrix1.dat"))
      val n = Matrix.loadFromStream[TDouble](getClass.getResourceAsStream("matrix2.dat"))
      val p = Matrix.loadFromStream[TDouble](getClass.getResourceAsStream("matrix3.dat"))

      val c = (m + n) * p

      c.saveToFile("matrix_res.dat")
  }
}
*/
