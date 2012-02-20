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
 * Vector
 */
class Vector[A](val size:Long)(implicit elem:Primitive[A]) extends Data {
  type ViewType = BufferView1D

  def allocate(memory:MemoryNode):BufferView1D = {
    //TODO: manage padding correctly
    val buffer = memory.allocate(elem.size*size)
    new BufferView1D(buffer, 0, elem.size*size)
  }

  def initialize(platform:Platform,f:(Long)=>A):Unit = {
    if (isDefined)
      throw new Exception("Trying to initialize a data already initialized")

    val view = allocate(platform.hostMemory)
    val buf = view.buffer.asInstanceOf[HostBuffer].peer
    
    for (x <- 0L until (view.size/4)) {
      val index = x*4 + view.offset
      elem.typ match {
        case "float" => buf.setFloat(index, f.asInstanceOf[(Long)=>Float](x))
        case "double" => buf.setDouble(index, f.asInstanceOf[(Long)=>Double](x))
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

      for (x <- 0L until (view.size/4)) {
        val index = x*4 + view.offset
        elem.typ match {
          case "float" => result.append(buf.getFloat(index) + " ")
          case "double" => result.append(buf.getDouble(index) + " ")
        }
      }

      FutureEvent(result.mkString)
    }
  }
}

