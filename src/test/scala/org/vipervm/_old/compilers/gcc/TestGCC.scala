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

package org.vipervm.compilers.gcc.tests

import org.scalatest.FunSuite

import org.vipervm.compilers.GCC

class TestGCC extends FunSuite {

  val src = """void dummy(int a, float b, float c, float * d) {
    *d = a ? b : c;
  }"""

  test("GCC Compiler") {
    val gcc = new GCC
    gcc.compile(src)
  }

}
