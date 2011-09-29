package org.vipervm.taskgraph

import java.io._

abstract class Task(val name:String, val args:Seq[Data]) {
  def split: TaskGraph
}

class TaskGraph(tasks:Seq[Task], deps:Map[Task,Task]) {

  private def dta(d:Data):(Int,String,Data) = d match {
    case a@InitialData(name) => (a.hashCode,name,a)
    case a@FilteredData(_,filter) => (a.hashCode,filter.name,a)
    case a@DataSelect(_,id) => (a.hashCode,id.toString,a)
  }

  private def dataHierarchy(d:Data):List[(Int,String,Data)] = d match {
    case a@InitialData(_) => List(dta(a))
    case a@FilteredData(src,_) => dta(a) :: dataHierarchy(src)
    case a@DataSelect(f,_) => dta(a) :: dataHierarchy(f)
  }

  def exportDOT(filename:String) = {

    val dataSet = tasks.flatMap(_.args).flatMap(dataHierarchy _).toSet
    val dataLinkSet = tasks.flatMap {
      task => task.args.flatMap {
        arg => (task.hashCode :: dataHierarchy(arg).map(_._1)).sliding(2).map(l => (l.head, l.drop(1).head))
      }
    }.toSet

    val dataLinkMap = tasks.flatMap {
      task => task.args.flatMap {
        arg => dataHierarchy(arg).map(_._3).sliding(2).filter(_.length == 2).map(l => (l.drop(1).head, l.head))
      }
    }.groupBy(a => a._1).mapValues(_.unzip._2.toSet)

    val initData = dataSet.filter {
      case a@(hash,_,InitialData(_)) => true
      case _ => false
    }.map(_._3)

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
/*    dataSet foreach { d => {
        val style = d._3 match {
          case InitialData(_) => "shape=doublecircle"
          case FilteredData(_,_) => "shape=diamond"
          case DataSelect(_,_) => "shape=circle"
        }
        f.println("\t\"%s\" [label=\"%s\",%s]".format(d._1, d._2, style))
      }
    }

    dataLinkSet foreach { d =>
      f.println("\t%s -> %s".format(d._1,d._2))
    }*/

    f.print("}\n")
    f.close
  }
}

sealed abstract class Data
case class InitialData(name:String) extends Data
case class FilteredData(source:Data, filter:Filter) extends Data
case class DataSelect(src:FilteredData, id:Int) extends Data

abstract class Filter {
  def name:String
}

class LineSplit extends Filter {
  val n = 10

  def apply(d:Data) = {
    val f = new FilteredData(d,this)
    for (i <- 1 to n) yield new DataSelect(f,i)
  }

  def name = "LineSplit x"+n
}
