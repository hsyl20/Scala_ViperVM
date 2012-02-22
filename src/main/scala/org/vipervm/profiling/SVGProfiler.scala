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

package org.vipervm.profiling

import org.vipervm.runtime.Task
import org.vipervm.platform.{Platform,DataTransfer,Processor}
import java.awt._
import java.io.{PrintWriter,FileWriter}
import javax.swing.SwingUtilities

import java.io.{OutputStream,OutputStreamWriter,FileOutputStream}
import org.apache.batik.svggen.SVGGraphics2D
import org.apache.batik.dom.svg.SVGDOMImplementation
import org.apache.batik.dom.util._
import org.apache.batik.swing.JSVGCanvas
import org.apache.batik.swing.svg.JSVGComponent

import org.w3c.dom.Document
import org.w3c.dom.DOMImplementation
import org.w3c.dom.svg.SVGDocument

class SVGProfiler(platform:Platform) extends Profiler {

  private var firstTimeStamp:Option[Long] = None

  val document = {
    val domImpl = SVGDOMImplementation.getDOMImplementation()
    val svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI
    domImpl.createDocument(svgNS, "svg", null).asInstanceOf[SVGDocument]
  }

  private val g = new SVGGraphics2D(document)
  val canvas = new JSVGCanvas
  canvas.setSVGDocument(document)
  canvas.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC)

  g.setSVGCanvasSize(new Dimension(400,400))

  val factor = 5e-8

  private var transfers:Map[DataTransfer,Long] = Map.empty
  private var tasks:Map[Task,Long] = Map.empty
  private val procs = platform.processors.zipWithIndex.toMap
  private val mems = platform.memories.zipWithIndex.toMap

  protected def reactions(e:ProfilingEvent):Unit = {
    val fst = firstTimeStamp.getOrElse {
      firstTimeStamp = Some(e.timestamp)
      firstTimeStamp.get
    }

    def position(time:Long) = ((time - fst) * factor).toInt

    e match {
      case DataTransferStart(data,dt) => {
        transfers += (dt -> e.timestamp)
      }
      case DataTransferEnd(data,dt) => {
        g.setPaint(Color.blue)
        val left = position(transfers(dt))
        val right = position(e.timestamp)
        val top = 100 + mems(dt.source.buffer.memory) * 25
        val bottom = 100 + mems(dt.target.buffer.memory) * 25
        if (right > g.getSVGCanvasSize.width) g.setSVGCanvasSize(new Dimension(right+20,400))
        g.drawLine(left,top,right,bottom)
        transfers -= dt
      }
      case TaskAssigned(task, proc) => {
      }
      case TaskStart(task,kernel,proc) => {
        tasks += (task -> e.timestamp)
      }
      case TaskCompleted(task, proc) => {
        g.setPaint(Color.green)
        val left = position(tasks(task))
        val right = position(e.timestamp)
        val top = 130 + procs(proc) * 25
        if (right > g.getSVGCanvasSize.width) g.setSVGCanvasSize(new Dimension(right+20,400))
        g.fill(new Rectangle(left, top, right-left, 20))
        tasks -= task
      }
    }

    g.getRoot(document.getRootElement)
    updateCanvas
  }

  def updateCanvas:Unit = {
    val r = new Runnable {
      def run:Unit = {
        canvas.setSVGDocument(document)
      }
    }
    SwingUtilities.invokeLater(r)
  }

  def save(filename:String):Unit = {
    val writer = new PrintWriter(new FileWriter(filename))
    stream(writer)
    writer.close
  }

  def print:Unit = {
    val writer = new PrintWriter(System.out)
    stream(writer)
    writer.close
  }

  def stream(writer:PrintWriter):Unit = {
    DOMUtilities.writeDocument(document,writer)
  }
}
