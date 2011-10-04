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

package org.vipervm.tests

import org.scalatest.FunSuite

import org.vipervm.taskgraph._


class TestTaskSplit extends FunSuite {

  val (x,y,z) = (1024,128,512)
  val typ = FloatType

  val adesc = MatrixDesc(x,y,typ)
  val bdesc = MatrixDesc(z,x,typ)
  val cdesc = MatrixDesc(z,y,typ)

  val a = new InitialData(adesc, "a")
  val b = new InitialData(bdesc, "b")
  val c = new InitialData(cdesc, "c")

  test("Matmult task can be created") {
    val m = new MatMul(a,b,c)
  }

  test("Matmult task can be split in lines") {
    val m = new MatMul(a,b,c)
    val ms = m.splits(0).apply
    ms.exportDOT("matmul_lines.dot")
    ms.exportC("matmul_lines.c")
  }

  test("Matmult task can be split in columns") {
    val m = new MatMul(a,b,c)
    val ms = m.splits(1).apply
    ms.exportDOT("matmul_columns.dot")
  }

  test("Matmult task can be split in blocks") {
    val m = new MatMul(a,b,c)
    val ms = m.splits(2).apply
    ms.exportDOT("matmul_blocks.dot")
    ms.exportC("matmul_blocks.c")
  }

  test("A node in a task graph can be replaced by a task graph") {
    val m = new MatMul(a,b,c)
    val ms = m.splits(0).apply
    val task = ms.tasks.flatMap {
      case m:MatMul => Some(m)
      case _ => None
    }.head
    val g = ms.replace(task, task.splits(0).apply)
    g.exportDOT("matmul_replaced.dot")
    ms.exportC("matmul_replaced.c")
  }
}
