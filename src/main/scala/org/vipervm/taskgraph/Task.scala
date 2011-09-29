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

package org.vipervm.taskgraph

import java.io._

abstract class Task(val name:String, val args:Seq[Data]) {
  def split: TaskGraph
}

class TaskGraph(tasks:Seq[Task], deps:Map[Task,Task]) {

  private def dataName(d:Data): String = d match {
    case InitialData(name) => name
    case FilteredData(_,filter) => filter.name
    case DataSelect(_,id) => id.toString
  }

  private def dta(d:Data):(Int,String,Data) = (d.hashCode, dataName(d), d)

  private def dataHierarchy(d:Data):List[(Int,String,Data)] = d match {
    case a@InitialData(_) => List(dta(a))
    case a@FilteredData(src,_) => dta(a) :: dataHierarchy(src)
    case a@DataSelect(f,_) => dta(a) :: dataHierarchy(f)
  }

  /** Data used by tasks in this graph */
  lazy val dataSet = tasks.flatMap(_.args).flatMap(dataHierarchy _).toSet

  /** Associations between data (data + filters + filtered data) */
  lazy val dataLinkMap = tasks.flatMap {
      task => task.args.flatMap {
        arg => dataHierarchy(arg).map(_._3).sliding(2).filter(_.length == 2).map(l => (l.drop(1).head, l.head))
      }
    }.groupBy(a => a._1).mapValues(_.unzip._2.toSet)

  /** Root data */
  lazy val initData = dataSet.filter {
      case a@(hash,_,InitialData(_)) => true
      case _ => false
    }.map(_._3)

  /**
   * Export the task graph .dot format
   */
  def exportDOT(filename:String) = {
    val taskSet = tasks.map(t => (t.hashCode, t.name)).toSet
    val taskLinkSet = deps.map(t => (t._1.hashCode, t._2.hashCode)).toSet


    val f = new PrintWriter(new FileOutputStream(filename))

    f.print("digraph G{\n")

    f.println("\tnode [shape=box]")
    taskSet foreach { t =>
      f.println("\t\"%s\" [label=\"%s\"]".format(t._1, t._2))
    }

    taskLinkSet foreach { t =>
      f.println("\t\"%s\" -> \"%s\"".format(t._1, t._2))
    }

    def printDataTree(d:Data):Unit = {
      val (hash,name,data) = dta(d)
      val style = data match {
        case InitialData(_) => "shape=doublecircle"
        case FilteredData(_,_) => "shape=diamond"
        case DataSelect(_,_) => "shape=circle"
      }
      f.println("\t\t\"%s\" [label=\"%s\",%s]".format(hash, name, style))

      for (children <- dataLinkMap.get(data); child <- children) {
        printDataTree(child)
        val (hash2,_,_) = dta(child)
        f.println("\t\t\"%s\" -> \"%s\"".format(hash2,hash))
      }
    }

    initData.foreach { d => 
      f.println("\tsubgraph \"cluster_%s\" {".format(d.hashCode))
      printDataTree(d)
      f.println("\t}")
    }

    for (t <- tasks; a <- t.args) {
      f.println("\t\"%s\" -> \"%s\"".format(t.hashCode,a.hashCode))
    }

    f.print("}\n")
    f.close
  }
}

sealed abstract class Data
case class InitialData(name:String) extends Data
case class FilteredData(source:Data, filter:Filter) extends Data
case class DataSelect(src:FilteredData, id:Int) extends Data

