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

package org.vipervm.runtime.data

import org.vipervm.runtime.data.Primitives._
import org.vipervm.platform._

/**
 * 2D matrix
 */
class Matrix2D[A](val width:Long, val height:Long)(implicit elem:Primitive[A]) extends Data {
  type ViewType = BufferView2D

  def allocate(memory:MemoryNode):BufferView2D = {
    //TODO: manage padding correctly
    val buffer = memory.allocate(elem.size*width*height)
    new BufferView2D(buffer, 0, elem.size*width, height, 0)
  }

  def initialize(platform:Platform,f:(Long,Long)=>A):Unit = {
    if (isDefined)
      throw new Exception("Trying to initialize a data already initialized")

    val view = allocate(platform.hostMemory)
    val buf = view.buffer.asInstanceOf[HostBuffer].peer
    
    for (y <- 0L until view.height; x <- 0L until (view.width/4)) {
      val index = x*4 + y * (view.width + view.rowPadding) + view.offset
      elem.typ match {
        case "float" => buf.setFloat(index, f.asInstanceOf[(Long,Long)=>Float](x,y))
        case "double" => buf.setDouble(index, f.asInstanceOf[(Long,Long)=>Double](x,y))
      }
    }

    store(view)
  }

  private def retrieveOnHost(platform:Platform):FutureEvent[ViewType] = {
    if (!isDefined)
      throw new Exception("Trying to retrieve uninitialized data")

    viewIn(platform.hostMemory) match {
      case Some(v) => FutureEvent(v)
      case None => {
        val view = allocate(platform.hostMemory)

        val sources = views
        val source = sources.head._2
        val link = platform.linkBetween(source,view).get
        val transfer = link.copy(source,view)
        val assocEvent = new UserEvent
        transfer.willTrigger {
          store(view)
          assocEvent.complete
        }
        FutureEvent(view, assocEvent)
      }
    }
  }

  def print(platform:Platform):FutureEvent[String] = {
    val fview = retrieveOnHost(platform)
    fview.fold {
      val view = fview()
      val buf = view.buffer.asInstanceOf[HostBuffer].peer
    
      val result = new StringBuilder

      for (y <- 0L until view.height) {
        for (x <- 0L until (view.width/4)) {
          val index = x*4 + y * (view.width + view.rowPadding) + view.offset
          elem.typ match {
            case "float" => result.append(buf.getFloat(index) + " ")
            case "double" => result.append(buf.getDouble(index) + " ")
          }
        }
        result.append("\n")
      }

      FutureEvent(result.mkString)
    }
  }
}

