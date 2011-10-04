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
import scala.math.abs

class TaskGraph(val tasks:Seq[Task], val deps:Seq[(Task,Task)]) {

  private def dataName(d:Data): String = d match {
    case InitialData(name) => name
    case FilteredData(_,filter) => filter.name
    case DataSelect(_,id@_*) => id.mkString(",")
    case TemporaryData(id) => "tmp"+id
  }

  private def dta(d:Data):(Int,String,Data) = (d.hashCode, dataName(d), d)

  private def dataHierarchy(d:Data):List[(Int,String,Data)] = d match {
    case a@InitialData(_) => List(dta(a))
    case a@TemporaryData(_) => List(dta(a))
    case a@FilteredData(src,_) => dta(a) :: dataHierarchy(src)
    case a@DataSelect(f,_*) => dta(a) :: dataHierarchy(f)
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
  lazy val initData = dataSet.flatMap {
    case (_,_,a@InitialData(_)) => Some(a)
    case _ => None
  }

  /** Temporary data */
  lazy val temporaryData = dataSet.flatMap {
    case (_,_,a@TemporaryData(_)) => Some(a)
    case _ => None
  }

  /** Filtered data */
  lazy val filteredData = dataSet.flatMap {
    case (_,_,a@FilteredData(_,_)) => Some(a)
    case _ => None
  }

  /** Filters */
  lazy val filters = dataSet.flatMap {
    case (_,_,FilteredData(_,filter)) => Some(filter)
    case _ => None
  }

  /**
   * Replace one of the node by a task graph
   */
  def replace(task:Task, graph:TaskGraph):TaskGraph = {
    val begin = new Marker
    val end = new Marker
    val beginDeps = deps filter (e => e._2 == task) map (e => (e._1,begin))
    val endDeps = deps filter (e => e._1 == task) map (e => (end,e._2))
    val oldDeps = deps filterNot(e => e._1 == task || e._2 == task) 
    val newDeps = oldDeps ++ beginDeps ++ endDeps
    val newNodes = graph.tasks ++ tasks.filterNot(_ == task)
    new TaskGraph(newNodes,newDeps)
  }


  /**
   * Export the task graph .dot format
   */
  def exportDOT(filename:String) = {
    val taskSet = tasks.map(t => (t.hashCode, t.name)).toSet
    val taskLinkSet = deps.map(t => (t._1.hashCode, t._2.hashCode)).toSet


    val f = new PrintWriter(new FileOutputStream(filename))

    f.println("digraph G{")
    f.println("\toverlap=false")
    f.println("\tsplines=true")
    f.println("\tsize=20480")

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
        case DataSelect(_,_*) => "shape=circle"
        case TemporaryData(_) => "shape=circle"
      }
      f.println("\t\t\"%s\" [label=\"%s\",color=goldenrod,%s]".format(hash, name, style))

      for (children <- dataLinkMap.get(data); child <- children) {
        printDataTree(child)
        val (hash2,_,_) = dta(child)
        f.println("\t\t\"%s\" -> \"%s\" [color=goldenrod]".format(hash2,hash))
      }
    }

    initData.foreach { d => 
      f.println("\tsubgraph \"cluster_%s\" {".format(d.hashCode))
      printDataTree(d)
      f.println("\t}")
    }

    f.println("\tsubgraph  \"cluster_temporary\" {")
    temporaryData.foreach(printDataTree _)
    f.println("\t}")

    for (t <- tasks; a <- t.args) {
      f.println("\t\"%s\" -> \"%s\" [color=cornflowerblue]".format(t.hashCode,a.hashCode))
    }

    f.println("}")
    f.close
  }

  def exportC(filename:String) = {
    val f = new PrintWriter(new FileOutputStream(filename))

    def printlnt(s:String) = f.println("\t"+s)
    def printlntt(s:String) = f.println("\t\t"+s)
    def printlnttt(s:String) = f.println("\t\t\t"+s)
    def tid(t:Object) = abs(t.hashCode)

    f.println("/* File automatically generated. Do not modify it or it may be overwritten */")
    f.println
    f.println("#include<stdio.h>")
    f.println("#include<stdlib.h>")
    f.println("#include<starpu.h>")
    f.println

    f.println("int main(int argc, char **argv) {")
    f.println

    printlnt("/* Initialize StarPU */")
    printlnt("starpu_init(NULL);")
    f.println

    printlnt("/* Create initial data */")
    for (d <- initData) {
      printlnt("float * data_%d = malloc(%d * sizeof(float));".format(tid(d), 10000))
    }
    f.println

    printlnt("/* Create StarPU buffers */")
    for (d <- initData) {
      printlnt("starpu_data_handle * handle_%d;".format(tid(d)))
      printlnt("starpu_data_matrix_register(&handle_%d, 0, (uintptr_t)data_%1$s, 100, 100, sizeof(float));".format(tid(d)))
    }
    f.println

    printlnt("/* Lazily allocate temporary data */")
    for (d <- temporaryData) {
      printlnt("starpu_data_handle * handle_%d;".format(tid(d)))
      printlnt("starpu_data_matrix_register(&handle_%d, 0, NULL, 100, 100, sizeof(float));".format(tid(d)))
    }
    f.println

    printlnt("/* Define filters */")

    def defineFilter(filter:Filter): Unit = filter match {
      case ColumnSplit(n) => {
        printlnt("struct starpu_data_filter fitler_%d_t{".format(tid(filter)))
        printlntt(".filter_func = starpu_vertical_block_filter_func,")
        printlntt(".nchildren = %d".format(n))
        printlnt("} * filter_%d;".format(tid(filter)))
      }
      case LineSplit(n) => {
        printlnt("struct starpu_data_filter filter_%d_t {".format(tid(filter)))
        printlntt(".filter_func = starpu_block_filter_func,")
        printlntt(".nchildren = %d".format(n))
        printlnt("} * filter_%d;".format(tid(filter)))
      }
      case BlockSplit(n,m) => {
        val lf = new LineSplit(n)
        val cf = new ColumnSplit(m)
        defineFilter(lf)
        defineFilter(cf)
        printlnt("struct starpu_data_filter * filter_vert_%d = filter_%d;".format(tid(filter), tid(cf)))
        printlnt("struct starpu_data_filter * filter_horiz_%d = filter_%d;".format(tid(filter), tid(lf)))
      }
    }

    filters.foreach(defineFilter _)
    f.println

    printlnt("/* Apply filters */")
    for (d <- filteredData) {
      val filter = d.filter
      filter match {
        case ColumnSplit(n) => printlnt("starpu_data_partition(handle_%d, filter_%d);".format(tid(d), tid(filter)))
        case LineSplit(n) => printlnt("starpu_data_partition(handle_%d, filter_%d);".format(tid(d), tid(filter)))
        case BlockSplit(n,m) => printlnt("starpu_data_map_filter(handle_%d, 2, filter_vert_%d, filter_horiz_%2$d);".format(tid(d), tid(filter)))
      }
    }
    f.println

    printlnt("/* Create tasks */")
    for (t <- tasks) {
      val task = "task_%d".format(tid(t))
      printlnt("struct starpu_task * %s = starpu_task_create();".format(task))
      printlnt("%s->cl = &codelet_%s;".format(task,t.name))
      for ((a,i) <- t.args.zipWithIndex) {
        a match {
          case d@InitialData(_) => printlnt("%s->buffers[%d] = handle_%d;".format(task,i,tid(d)))
          case d@TemporaryData(_) => printlnt("%s->buffers[%d] = handle_%d;".format(task,i,tid(d)))
          case DataSelect(src,idx@_*) => printlnt("%s->buffers[%d] = starpu_data_get_sub_data(handle_%d,%d,%s);".format(
            task,i,tid(src),idx.length,idx.mkString(",")
          ))
          case _ => throw new Exception("Invalid data type for task parameter")
        }
      }
    }
    f.println

    printlnt("/* Set task dependencies */")
    printlnt("{")
    for (t <- tasks) {
      val ds = deps.collect{ case (t1,t2) if t2 == t => t1 }
      if (ds.length != 0) {
        val dss = ds.map("task_" + tid(_)).mkString("{", ",", "}")
        printlntt("struct starpu_task * deps_%d[%d] = %s;".format(tid(t), ds.length, dss))
        printlntt("starpu_task_declare_deps_array(task_%d, %d, deps_%1$d);".format(tid(t), ds.length))
      }
    }
    printlnt("}")
    f.println
    
    printlnt("/* Submit tasks */")
    for (t <- tasks) {
      printlnt("starpu_task_submit(task_%d);".format(tid(t)))
    }
    f.println

    printlnt("/* Wait for tasks */")
    printlnt("starpu_task_wait_for_all();")
    f.println


    printlnt("/* Destroy tasks */")
    for (t <- tasks) {
      printlnt("starpu_task_destroy(task_%d);".format(tid(t)))
    }
    f.println

    printlnt("/* Unregister initial data */")
    for (d <- initData) {
      printlnt("starpu_data_unregister(handle_%d);".format(tid(d)))
    }
    f.println

    printlnt("/* Unregister temporary data */")
    for (d <- temporaryData) {
      printlnt("starpu_data_unregister(handle_%d);".format(tid(d)))
    }
    f.println

    printlnt("/* Shutdown */")
    printlnt("starpu_shutdown();")
    f.println

    printlnt("return 0;")
    f.println("}")

    f.close
  }
}
