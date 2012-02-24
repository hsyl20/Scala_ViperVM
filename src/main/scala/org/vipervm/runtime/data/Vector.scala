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
 * Vector
 */
class Vector[A](val size:Long)(implicit elem:Primitive[A]) extends Data with PrintableData {
  type ViewType = BufferView1D

  def allocate(memory:MemoryNode):BufferView1D = {
    //TODO: manage padding correctly
    val buffer = memory.allocate(elem.size*size)
    new BufferView1D(buffer, 0, elem.size*size)
  }

  def initialize(dataManager:DataManager,f:(Long)=>A):Unit = {
    if (isDefined)
      throw new Exception("Trying to initialize a data already initialized")

    onHost(dataManager) { (view,buf) => {
      for (x <- 0L until (view.size/4)) {
        val index = x*4 + view.offset
        elem.typ match {
          case "float" => buf.peer.setFloat(index, f.asInstanceOf[(Long)=>Float](x))
          case "double" => buf.peer.setDouble(index, f.asInstanceOf[(Long)=>Double](x))
        }
      }
    }}

  }

  override protected def hostPrint(view:ViewType,buffer:HostBuffer):String = {
    val mem = buffer.peer
    
    val result = new StringBuilder

    for (x <- 0L until (view.size/4)) {
      val index = x*4 + view.offset
      elem.typ match {
        case "float" => result.append(mem.getFloat(index) + " ")
        case "double" => result.append(mem.getDouble(index) + " ")
      }
    }

    result.mkString
  }
}

