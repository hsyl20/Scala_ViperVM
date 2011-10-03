package org.vipervm.tests

import org.scalatest.FunSuite

import org.vipervm.taskgraph._


class MatmultTask(a:Data,b:Data,c:Data) extends Task("matmult", Seq(a,b,c)) with Splittable {
  def split:TaskGraph = {
    val ls = new LineSplit
    val as = ls(a)
    val cs = ls(c)
    val tasks = (as zip cs) map (t => new MatmultTask(t._1, b, t._2))
    new TaskGraph(tasks, Nil)
  }
}

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
    val ms = m.split
    ms.exportDOT("matmult.dot")
  }

  test("A node in a task graph can be replaced by a task graph") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatmultTask(a,b,c)
    val ms = m.split
    val task = ms.tasks.flatMap {
      case m:MatmultTask => Some(m)
      case _ => None
    }.first
    val g = ms.replace(task, task.split)
    g.exportDOT("matmult.dot")
  }
}
