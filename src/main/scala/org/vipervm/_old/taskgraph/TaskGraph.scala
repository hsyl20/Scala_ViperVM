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
import org.vipervm.platform.{ReadOnly,ReadWrite}
import org.vipervm.utils._


class TaskGraph(val tasks:Seq[Task], val deps:Seq[(Task,Task)]) {

  /** Data used by tasks in this graph */
  lazy val dataSet = tasks.flatMap(_.args).toSet
  /** Data used by tasks and parent data */
  lazy val allDataSet = dataSet.flatMap(dataHierarchy _).toSet

  val (maxDataSize,minDataSize,meanDataSize) = {
    val sizeSet = dataSet.map { _.desc match {
      case MatrixDesc(m,n,typ) => m*n*typ.size
      case _ => ???
    }}
    (sizeSet.max.toFloat, sizeSet.min.toFloat, sizeSet.sum.toFloat/sizeSet.size.toFloat)
  }

  /** Statistics string */
  def statistics = "Data: Count=%d MaxSize=%.2fMB MinSize=%.2fMB MeanSize=%.2fMB".format(
    dataSet.size, maxDataSize/1024.0, minDataSize/1024.0, meanDataSize/1024.0
  )


  /** Associations between data (data + filters + filtered data) */
  lazy val dataLinkMap = tasks.flatMap {
    task => task.args.flatMap {
      arg => dataHierarchy(arg).map(_._3).sliding(2).filter(_.length == 2).map(l => (l.drop(1).head, l.head))
    }
  }.groupBy(a => a._1).mapValues(_.unzip._2.toSet)

  /** Root data */
  lazy val initData = allDataSet.flatMap {
    case (_,_,a@InitialData(_,_,_)) => Some(a)
    case _ => None
  }

  /** Temporary data */
  lazy val temporaryData = allDataSet.flatMap {
    case (_,_,a@TemporaryData(_,_)) => Some(a)
    case _ => None
  }

  /** Filtered data */
  lazy val filteredData = allDataSet.flatMap {
    case (_,_,a@FilteredData(_,_)) => Some(a)
    case _ => None
  }

  /** Filters */
  lazy val filters = allDataSet.flatMap {
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

  private def dataName(d:Data): String = d match {
    case InitialData(_,_,name) => name
    case FilteredData(_,filter) => filter.name
    case DataSelect(_,id@_*) => id.mkString(",")
    case TemporaryData(_,id) => "tmp"+id
  }

  private def dta(d:Data):(Int,String,Data) = (d.hashCode, dataName(d), d)

  private def dataHierarchy(d:Data):List[(Int,String,Data)] = d match {
    case a@InitialData(_,_,_) => List(dta(a))
    case a@TemporaryData(_,_) => List(dta(a))
    case a@FilteredData(src,_) => dta(a) :: dataHierarchy(src)
    case a@DataSelect(f,_*) => dta(a) :: dataHierarchy(f)
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
        case InitialData(_,_,_) => "shape=doublecircle"
        case TemporaryData(_,_) => "shape=circle"
        case FilteredData(_,_) => "shape=diamond"
        case DataSelect(_,_*) => "shape=circle"
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

  def exportC(filename:String,verbose:Boolean=true) = {
    val f = new PrintWriter(new FileOutputStream(filename))

    def printlnt(s:String) = f.println("\t"+s)
    def printlntt(s:String) = f.println("\t\t"+s)
    def printlnttt(s:String) = f.println("\t\t\t"+s)
    def printlntttt(s:String) = f.println("\t\t\t\t"+s)
    def comm(s:String) = if (!verbose) ("/* " + s + "*/") else ("printf(\"<-- "+s+" -->\\n\");")
    def comment(s:String) = f.println(comm(s))
    def commentt(s:String) = f.println("\t"+comm(s))

    def tid(t:Object) = abs(t.hashCode)

    f.println("/* File automatically generated. Do not modify it or it may be overwritten */")
    f.println
    f.println("#include<stdio.h>")
    f.println("#include<stdlib.h>")
    f.println("#include<starpu.h>")
    f.println

    printlnt("/* Create codelets */")
    for (t <- tasks.groupBy(_.name).map(a => a._2.head)) {
      printlnt(t.source)
    }
    f.println


    f.println("int main(int argc, char **argv) {")
    f.println

    commentt("Initialize StarPU")
    printlnt("starpu_init(NULL);")
    f.println

    commentt("Allocate initial data")

    def dataSize(d:Data) = d.desc match {
      case MatrixDesc(m,n,typ) => "sizeof(%s)*%d*%d".format(typ2c(typ),m,n)
      case _ => throw new Exception("Data type not supported")
    }

    def elemType(d:Data) = d.desc match {
      case MatrixDesc(_,_,typ) => typ
      case _ => throw new Exception("Data type not supported")
    }

    for (d <- initData) {
      printlnt("%s * data_%d = (%1$s *)malloc(%s);\t// \"%s\"".format(typ2c(elemType(d)), tid(d), dataSize(d), d.name))
    }
    f.println

    commentt("Initialize data")

    def zeroInit(d:Data): Unit = d.desc match {
      case MatrixDesc(m,n,typ) => printlnt("memset(data_%d, 0, %s);".format(tid(d), dataSize(d)))
      case _ => throw new Exception("Unsupported data type")
    }

    def randomInit(d:Data): Unit = d.desc match {
      case MatrixDesc(m,n,typ) => {
        printlnt("{")
        printlntt("int init_%d_y, init_%1$d_x;".format(tid(d)))
        printlntt("for (init_%d_y = 0; init_%1$d_y < %d; init_%1$d_y++) {".format(tid(d),n))
        printlnttt("for (init_%d_x = 0; init_%1$d_x < %d; init_%1$d_x++) {".format(tid(d),m))
        printlntttt("data_%d[init_%1$d_y*%d+init_%1$d_x] = (%s)drand48();".format(tid(d), m, typ2c(typ)))
        printlnttt("}")
        printlntt("}")
        printlnt("}")
      }
      case _ => throw new Exception("Unsupported data type")
    }

    for (d <- initData) {
      d.init match {
        case NoInit => ()
        case ZeroInit => zeroInit(d)
        case RandomInit => randomInit(d)
      }
    }
    f.println

    def typ2c(dt:DataType) = dt match {
      case FloatType => "float"
      case DoubleType => "double"
    }

    def registerData(d:Data,init:Boolean): Unit = {
      val ptr = if (init) "(uintptr_t)data_%d".format(tid(d)) else "NULL"

      printlnt("starpu_data_handle handle_%d;".format(tid(d)))
      d.desc match {
        case MatrixDesc(m,n,typ) => printlnt("starpu_matrix_data_register(&handle_%d, 0, %s, 0, %d, %d, sizeof(%s));".format(tid(d),ptr,m,n,typ2c(typ)))
        case _ => throw new Exception("Data type not supported")
      }
    }

    commentt("Create StarPU buffers")
    initData.foreach(registerData(_,true))
    f.println

    commentt("Lazily allocate temporary data")
    temporaryData.foreach(registerData(_,false))
    f.println

    commentt("Define filters")

    def defineFilter(filter:Filter): Unit = filter match {
      case ColumnSplit(n) => {
        printlnt("struct starpu_data_filter filter_%d_t = {".format(tid(filter)))
        printlntt(".filter_func = starpu_vertical_block_filter_func,")
        printlntt(".nchildren = %d".format(n))
        printlnt("};")
        printlnt("struct starpu_data_filter * filter_%d = &filter_%1$d_t;".format(tid(filter)))
      }
      case LineSplit(n) => {
        printlnt("struct starpu_data_filter filter_%d_t = {".format(tid(filter)))
        printlntt(".filter_func = starpu_block_filter_func,")
        printlntt(".nchildren = %d".format(n))
        printlnt("};")
        printlnt("struct starpu_data_filter * filter_%d = &filter_%1$d_t;".format(tid(filter)))
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

    commentt("Apply filters")
    for (d <- filteredData) {
      val filter = d.filter
      d.source match {
        case InitialData(_,_,_) => ()
        //TODO FIXME: hierarchy of filters should be supported !!!
        case _ => printlnt("/* FIXME : this filter won't work as hierarchy of filters are not supported yet!!! */")
      }
      filter match {
        case ColumnSplit(n) => printlnt("starpu_data_partition(handle_%d, filter_%d);".format(tid(d.source), tid(filter)))
        case LineSplit(n) => printlnt("starpu_data_partition(handle_%d, filter_%d);".format(tid(d.source), tid(filter)))
        case BlockSplit(n,m) => printlnt("starpu_data_map_filter(handle_%d, 2, filter_vert_%d, filter_horiz_%2$d);".format(tid(d.source), tid(filter)))
      }
    }
    f.println

    commentt("Create tasks")
    for (t <- tasks) {
      val task = "task_%d".format(tid(t))
      printlnt("struct starpu_task * %s = starpu_task_create();".format(task))
      printlnt("%s->cl = &codelet_%s;".format(task,t.name))
      for (((a,mode),i) <- (t.args zip t.argModes).zipWithIndex) {
        a match {
          case d@InitialData(_,_,_) => printlnt("%s->buffers[%d].handle = handle_%d;".format(task,i,tid(d)))
          case d@TemporaryData(_,_) => printlnt("%s->buffers[%d].handle = handle_%d;".format(task,i,tid(d)))
          case DataSelect(src,idx@_*) => printlnt("%s->buffers[%d].handle = starpu_data_get_sub_data(handle_%d,%d,%s);".format(
            task,i,tid(src.source),idx.length,idx.mkString(",")
          ))
          case _ => throw new Exception("Invalid data type for task parameter")
        }
        mode match {
          case ReadOnly => printlnt("%s->buffers[%d].mode = STARPU_R;".format(task,i))
          case ReadWrite => printlnt("%s->buffers[%d].mode = STARPU_RW;".format(task,i))
        }
      }
      f.println
    }

    commentt("Set task dependencies")
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
    
    commentt("Submit tasks")
    for (t <- tasks) {
      printlnt("starpu_task_submit(task_%d);".format(tid(t)))
    }
    f.println

    commentt("Wait for tasks")
    printlnt("starpu_task_wait_for_all();")
    f.println

    /* Tasks are automatically destroyed...
    commentt("Destroy tasks")
    for (t <- tasks) {
      printlnt("starpu_task_destroy(task_%d);".format(tid(t)))
    }
    f.println
    */

    commentt("Unregister initial data")
    for (d <- initData) {
      printlnt("starpu_data_unregister(handle_%d);".format(tid(d)))
    }
    f.println

    commentt("Unregister temporary data")
    for (d <- temporaryData) {
      printlnt("starpu_data_unregister(handle_%d);".format(tid(d)))
    }
    f.println

    commentt("Shutdown")
    printlnt("starpu_shutdown();")
    f.println

    printlnt("return 0;")
    f.println("}")

    f.close
  }
}