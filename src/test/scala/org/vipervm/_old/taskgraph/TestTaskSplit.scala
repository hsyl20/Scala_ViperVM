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

package org.vipervm.tests.taskgraph

import org.scalatest.FunSuite

import org.vipervm.taskgraph._


class TestTaskSplit extends FunSuite {

  val (x,y,z) = (1024,128,512)
  val typ = FloatType

  val adesc = MatrixDesc(x,y,typ)
  val bdesc = MatrixDesc(z,x,typ)
  val cdesc = MatrixDesc(z,y,typ)

  val a = new InitialData(adesc, RandomInit, "a")
  val b = new InitialData(bdesc, RandomInit, "b")
  val c = new InitialData(cdesc, NoInit, "c")

  test("Matmult task can be created") {
    val m = new MatMul(a,b,c)
  }

  test("Matmult task can be split in lines") {
    val m = new MatMul(a,b,c)
    val ms = m.splits('lines).apply
    ms.exportDOT("matmul_lines.dot")
    ms.exportC("matmul_lines.c")
    println(ms.statistics)
  }

  test("Matmult task can be split in columns") {
    val m = new MatMul(a,b,c)
    val ms = m.splits('columns).apply
    ms.exportDOT("matmul_columns.dot")
    ms.exportC("matmul_columns.c")
    println(ms.statistics)
  }

  test("Matmult task can be split in blocks") {
    val m = new MatMul(a,b,c)
    val ms = m.splits('blocks).apply
    ms.exportDOT("matmul_blocks.dot")
    ms.exportC("matmul_blocks.c")
    println(ms.statistics)
  }

  test("A node in a task graph can be replaced by a task graph") {
    val m = new MatMul(a,b,c)
    val ms = m.splits('lines).apply
    val task = ms.tasks.flatMap {
      case m:MatMul => Some(m)
      case _ => None
    }.head
    val g = ms.replace(task, task.splits('lines).apply)
    g.exportDOT("matmul_replaced.dot")
    g.exportC("matmul_replaced.c")
    println(g.statistics)
  }
}
