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

  test("Matmult task can be created") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatMul(a,b,c)
  }

  test("Matmult task can be split in lines") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatMul(a,b,c)
    val ms = m.splits(0).apply
    ms.exportDOT("matmul_lines.dot")
  }

  test("Matmult task can be split in columns") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatMul(a,b,c)
    val ms = m.splits(1).apply
    ms.exportDOT("matmul_columns.dot")
  }

  test("Matmult task can be split in blocks") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatMul(a,b,c)
    val ms = m.splits(2).apply
    ms.exportDOT("matmul_blocks.dot")
  }

  test("A node in a task graph can be replaced by a task graph") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatMul(a,b,c)
    val ms = m.splits(0).apply
    val task = ms.tasks.flatMap {
      case m:MatMul => Some(m)
      case _ => None
    }.head
    val g = ms.replace(task, task.splits(0).apply)
    g.exportDOT("matmul_replaced.dot")
  }
}
