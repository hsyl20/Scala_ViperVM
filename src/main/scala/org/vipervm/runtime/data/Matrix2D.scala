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
import org.vipervm.runtime.mm.DataManager

/**
 * 2D matrix
 */
class Matrix2D[A](val width:Long, val height:Long)(implicit elem:Primitive[A]) extends Data with PrintableData {
  type ViewType = BufferView2D

  def allocate(memory:MemoryNode):BufferView2D = {
    //TODO: manage padding correctly
    val buffer = memory.allocate(elem.size*width*height)
    new BufferView2D(buffer, 0, elem.size*width, height, 0)
  }

  def initialize(dataManager:DataManager,f:(Long,Long)=>A):Unit = {
    if (isDefined)
      throw new Exception("Trying to initialize a data already initialized")

    onHost(dataManager) { (view,buf) => {
      for (y <- 0L until view.height; x <- 0L until (view.width/4)) {
        val index = x*4 + y * (view.width + view.rowPadding) + view.offset
        elem.typ match {
          case "float" => buf.peer.setFloat(index, f.asInstanceOf[(Long,Long)=>Float](x,y))
          case "double" => buf.peer.setDouble(index, f.asInstanceOf[(Long,Long)=>Double](x,y))
        }
      }
    }}.syncWait

  }

  override protected def hostPrint(view:ViewType,buffer:HostBuffer):String = {
    val mem = buffer.peer

    val result = new StringBuilder

    for (y <- 0L until view.height) {
      for (x <- 0L until (view.width/4)) {
        val index = x*4 + y * (view.width + view.rowPadding) + view.offset
        elem.typ match {
          case "float" => result.append(mem.getFloat(index) + " ")
          case "double" => result.append(mem.getDouble(index) + " ")
        }
      }
      result.append("\n")
    }

    result.mkString
  }
}

