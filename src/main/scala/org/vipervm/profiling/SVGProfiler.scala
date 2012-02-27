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
import java.awt.geom.RoundRectangle2D
import java.io.{PrintWriter,FileWriter,OutputStream,OutputStreamWriter,FileOutputStream}
import javax.swing.SwingUtilities

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

  val initSize = new Dimension(800,600)
  val barHeight = 25

  val document = {
    val domImpl = SVGDOMImplementation.getDOMImplementation()
    val svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI
    domImpl.createDocument(svgNS, "svg", null).asInstanceOf[SVGDocument]
  }

  private val g = new SVGGraphics2D(document)
  val canvas = new JSVGCanvas
  canvas.setSVGDocument(document)
  g.setSVGCanvasSize(initSize)



  val memTops = (platform.memories zip platform.memories.map( mem =>
    (1 + platform.processorsThatCanWorkIn(mem).length) * barHeight
  ).scanLeft(0)(_+_).map(_+150)).toMap

  val procTops = platform.memories.flatMap( mem => {
    val procs = platform.processorsThatCanWorkIn(mem)
    val top = memTops(mem)
    procs.zipWithIndex.map { case (p,t) =>
      (p,t*barHeight + top + barHeight)
    }
  }).toMap

  /* Draw legend */
  procTops.zipWithIndex.foreach { case ((proc,top),idx) => {
    g.drawString("%d) %s".format(idx,proc.toString), 0.0f, (1+idx)*barHeight)
    g.drawString("%d)".format(idx), 0.0f, top + barHeight/2)
  }}

  val factor = 5e-8

  private var transfers:Map[DataTransfer,Long] = Map.empty
  private var tasks:Map[Task,Long] = Map.empty

  protected def reactions(e:ProfilingEvent):Unit = {
    val fst = firstTimeStamp.getOrElse {
      firstTimeStamp = Some(e.timestamp)
      firstTimeStamp.get
    }

    def position(time:Long) = ((time - fst) * factor + 20).toInt

    e match {
      case DataTransferStart(data,dt) => {
        transfers += (dt -> e.timestamp)
      }
      case DataTransferEnd(data,dt) => {
        val color = new Color(0.0f, 0.0f, 1.0f, 0.6f)
        g.setPaint(color)
        val stroke = new BasicStroke(2.0f)
        g.setStroke(stroke)
        val left = position(transfers(dt))
        val right = position(e.timestamp)
        val top = memTops(dt.source.buffer.memory) + (barHeight/2)
        val bottom = memTops(dt.target.buffer.memory) + (barHeight/2)
        if (right > g.getSVGCanvasSize.width) g.setSVGCanvasSize(new Dimension(right+20,g.getSVGCanvasSize.height))
        g.drawLine(left,top,right,bottom)
        transfers -= dt
      }
      case TaskAssigned(task, proc) => {
      }
      case TaskStart(task,kernel,proc) => {
        tasks += (task -> e.timestamp)
      }
      case TaskCompleted(task, proc) => {
        val color = new Color(0.0f, 1.0f, 0.0f)
        g.setPaint(color)
        val stroke = new BasicStroke(1.0f)
        g.setStroke(stroke)
        val left = position(tasks(task))
        val right = position(e.timestamp)
        val top = procTops(proc) + 2
        if (right > g.getSVGCanvasSize.width) g.setSVGCanvasSize(new Dimension(right+20,g.getSVGCanvasSize.height))
        val shape = new RoundRectangle2D.Float(left, top, right-left, barHeight-4, 10.0f, 10.0f) 
        //g.draw(shape)
        g.fill(shape)
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
