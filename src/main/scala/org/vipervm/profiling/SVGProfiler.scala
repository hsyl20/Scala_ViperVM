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
import org.vipervm.platform.DataTransfer
import java.awt._

import java.io.{OutputStream,OutputStreamWriter,FileOutputStream}
import org.apache.batik.svggen.SVGGraphics2D
import org.apache.batik.dom.svg.SVGDOMImplementation
import org.apache.batik.swing.JSVGCanvas
import org.apache.batik.swing.svg.JSVGComponent

import org.w3c.dom.Document
import org.w3c.dom.DOMImplementation
import org.w3c.dom.svg.SVGDocument

class SVGProfiler extends Profiler {

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
        if (right > g.getSVGCanvasSize.width) g.setSVGCanvasSize(new Dimension(right+20,400))
        g.fill(new Rectangle(left, 100, right-left, 20))
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
        if (right > g.getSVGCanvasSize.width) g.setSVGCanvasSize(new Dimension(right+20,400))
        g.fill(new Rectangle(left, 130, right-left, 20))
        tasks -= task
      }
    }

    g.getRoot(document.getRootElement)

  }

  def save(filename:String):Unit = stream(new FileOutputStream(filename))

  def print:Unit = stream(System.out)

  def stream(stream:OutputStream):Unit = {
    val out = new OutputStreamWriter(stream, "UTF-8")
    g.stream(out, true)
  }
}
