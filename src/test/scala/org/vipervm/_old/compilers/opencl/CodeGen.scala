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

package org.vipervm.tests.codegen

import org.scalatest.FunSuite

import org.vipervm.codegen.opencl._

class TestCodeGen extends FunSuite {

  test("Basic code generation") {
    val code = new CLCode {
      'a :- CFloat := "1.0"
      'b :- CFloat := "2.0"
      'c := 'a + 'b
    }

    println(code)
  }
}
