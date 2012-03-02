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

import org.vipervm.platform._
import org.vipervm.runtime._

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

import akka.actor.{TypedActor,ActorSystem,TypedProps}

trait SVGProfiler extends Profiler {
  def save(filename:String):Unit
  def stream(writer:PrintWriter):Unit
  def canvas:JSVGCanvas
}

private class SVGProfilerImpl(platform:Platform) extends SVGProfiler {

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

  val procTopsList = platform.memories.flatMap( mem => {
    val procs = platform.processorsThatCanWorkIn(mem)
    val top = memTops(mem)
    procs.zipWithIndex.map { case (p,t) =>
      (p,t*barHeight + top + barHeight)
    }
  })

  val procTops = procTopsList.toMap

  /* Draw legend */
  procTopsList.zipWithIndex.foreach { case ((proc,top),idx) => {
    g.drawString("%d) %s".format(idx,proc.toString), 0.0f, (1+idx)*(barHeight-10))
    g.drawString("%d)".format(idx), 0.0f, top + barHeight/2)
  }}

  private var transfers:Map[DataTransfer,Long] = Map.empty
  private var tasks:Map[Task,Long] = Map.empty

  val margin = 30
  val scaleTop = procTopsList.last._2 + 2*barHeight
  g.drawString("  msecs", 0.0f, (scaleTop+25).toFloat)

  protected def drawScale(start:Int,end:Int,text:String): Unit = {
    val color = new Color(0.0f, 0.0f, 0.0f, 0.6f)
    g.setPaint(color)
    val stroke = new BasicStroke(1.0f)
    g.setStroke(stroke)

    g.drawLine(start,scaleTop-4,start, scaleTop)
    g.drawLine(start, scaleTop, end, scaleTop)
    g.drawLine(end,scaleTop,end,scaleTop-4)
    val metrics = g.getFontMetrics(g.getFont)
    val textWidth = metrics.stringWidth(text)
    g.drawString(text, end.toFloat-(textWidth/2.0f), (scaleTop+25).toFloat)
    val right = end + (textWidth/2)
    if (right > g.getSVGCanvasSize.width) resizeCanvas(new Dimension(right+20,g.getSVGCanvasSize.height))
  }

  def resizeCanvas(size:Dimension, old:Dimension = g.getSVGCanvasSize):Unit = {
    g.setSVGCanvasSize(size)
  }


  protected var scaleRightTimeStamp:Long = 0L

  protected val scale = 5e-7

  def first(current:Long):Long = {
    firstTimeStamp.getOrElse {
      firstTimeStamp = Some(current)
      scaleRightTimeStamp = current
      current
    }
  }

  def drawScaleTo(time:Long):Unit = {
    while (time > scaleRightTimeStamp) {
      val newEnd = scaleRightTimeStamp + 1e8.toLong
      drawScale(position(scaleRightTimeStamp), position(newEnd), ((newEnd-first(time))/1e6.toLong).toString)
      scaleRightTimeStamp = newEnd
    }
  }


  private def position(time:Long) = ((time - first(time)) * scale + margin).toInt
  private def time(position:Int) = ((position - margin).toDouble / scale) + first(0L)
    
  def transferStart(data:MetaView,dataTransfer:DataTransfer,timestamp:Long):Unit = {
    transfers += (dataTransfer -> timestamp)
  }

  def transferEnd(data:MetaView,dataTransfer:DataTransfer,timestamp:Long):Unit = {
    val color = new Color(0.0f, 0.0f, 1.0f, 0.5f)
    g.setPaint(color)
    val stroke = new BasicStroke(2.0f)
    g.setStroke(stroke)
    val left = position(transfers(dataTransfer))
    val right = position(timestamp)
    val top = memTops(dataTransfer.source.buffer.memory) + (barHeight/2)
    val bottom = memTops(dataTransfer.target.buffer.memory) + (barHeight/2)
    if (right > g.getSVGCanvasSize.width) resizeCanvas(new Dimension(right+20,g.getSVGCanvasSize.height))
    g.drawLine(left,top,right,bottom)
    transfers -= dataTransfer

    drawScaleTo(timestamp)
    updateCanvas
  }

  def taskAssigned(task:Task,proc:Processor,timestamp:Long):Unit = {
  }

  def taskStart(task:Task,kernel:Kernel,proc:Processor,timestamp:Long):Unit = {
    tasks += (task -> timestamp)
  }

  def taskCompleted(task:Task,proc:Processor,timestamp:Long):Unit = {
    val color = new Color(0.0f, 1.0f, 0.0f,0.5f)
    g.setPaint(color)
    val stroke = new BasicStroke(1.0f)
    g.setStroke(stroke)
    val left = position(tasks(task))
    val right = position(timestamp)
    val top = procTops(proc) + 2
    if (right > g.getSVGCanvasSize.width) resizeCanvas(new Dimension(right+20,g.getSVGCanvasSize.height))
    val shape = new RoundRectangle2D.Float(left, top, right-left, barHeight-4, 10.0f, 10.0f) 
    g.fill(shape)
    tasks -= task

    drawScaleTo(timestamp)
    updateCanvas
  }

  private def updateCanvas:Unit = {
    g.getRoot(document.getRootElement)

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


object SVGProfiler {
  
  protected val factory = TypedActor.get(ActorSystem())

  def apply(platform:Platform):SVGProfiler = {
    factory.typedActorOf( TypedProps(classOf[SVGProfiler],new SVGProfilerImpl(platform)))
  }

}
