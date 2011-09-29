package org.vipervm.tests

import org.scalatest.FunSuite

import org.vipervm.taskgraph._


class MatmultTask(a:Data,b:Data,c:Data) extends Task("matmult", Seq(a,b,c)) {
  def split:TaskGraph = {
    val ls = new LineSplit
    val as = ls(a)
    val cs = ls(c)
    val tasks = (as zip cs) map (t => new MatmultTask(t._1, b, t._2))
    new TaskGraph(tasks, Map.empty)
  }
}

class TestTaskSplit extends FunSuite {

  test("Matmult task can be created") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatmultTask(a,b,c)
    println(m)
  }

  test("Matmult task can be split") {
    val a = new InitialData("a")
    val b = new InitialData("b")
    val c = new InitialData("c")
    val m = new MatmultTask(a,b,c)
    val ms = m.split
    ms.exportDOT("matmult.dot")
    println(ms)
  }
}
