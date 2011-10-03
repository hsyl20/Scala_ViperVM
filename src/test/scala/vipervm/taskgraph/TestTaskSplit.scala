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
    val m = new MatmultTask(a,b,c)
  }

  test("Matmult task can be split") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatmultTask(a,b,c)
    val ms = m.splits(0).apply
    ms.exportDOT("matmult.dot")
  }

  test("A node in a task graph can be replaced by a task graph") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatmultTask(a,b,c)
    val ms = m.splits(0).apply
    val task = ms.tasks.flatMap {
      case m:MatmultTask => Some(m)
      case _ => None
    }.head
    val g = ms.replace(task, task.splits(0).apply)
    g.exportDOT("matmult_replaced.dot")
  }
}
