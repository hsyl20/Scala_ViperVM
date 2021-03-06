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

package org.vipervm.platform

trait MemoryCopier {
  /**
   * Schedule an asynchronous copy of data
   *
   * @param link    Link handling the transfer
   * @param source  Source buffer view
   * @param target  Target buffer view
   */
  def copy(link:Link,source:BufferView,target:BufferView):DataTransfer =
    throw new Exception("Unsupported copy")
}

/**
 * Link support for 1D copy
 */
trait Copy1DSupport extends MemoryCopier {
  /**
   * Schedule an asynchronous copy of 1D data
   *
   * @param link    Link handling the transfer
   * @param source  Source buffer view
   * @param target  Target buffer view
   */
  def copy1D(link:Link,source:BufferView1D,target:BufferView1D):DataTransfer


  override def copy(link:Link,source:BufferView,target:BufferView):DataTransfer = {
    
    if (source.buffer.memory != link.source || target.buffer.memory != link.target)
      throw new Exception("Invalid copy: link cannot transfer between the given buffers")
    
    (source,target) match {
      case (src:BufferView1D,tgt:BufferView1D) => {
        if (src.size != tgt.size)
          throw new Exception("Invalid copy: different view sizes (%d,%d)".format(src.size,tgt.size))
        val ev = copy1D(link,src,tgt)
        AsyncGC.add(ev, source, target)
        ev
      }
      case (src:BufferView2D,tgt:BufferView2D) if (src.padding == 0 && tgt.padding == 0) => {
        if (src.width != tgt.width || src.height != tgt.height)
          throw new Exception("Invalid copy: different view dimensions: (%d,%d) vs (%d,%d)".format(
            src.width,src.height, tgt.width, tgt.height))
        val tempSrc = BufferView1D(src.buffer,src.offset,src.width*src.height)
        val tempTgt = BufferView1D(tgt.buffer,tgt.offset,tgt.width*tgt.height)
        val ev = copy1D(link,tempSrc,tempTgt)
        AsyncGC.add(ev, source, target, tempSrc, tempTgt)
        ev
      }
      case _ => super.copy(link,source,target)
    }
  }
}

/**
 * Link support for 2D copy
 */
trait Copy2DSupport extends MemoryCopier {
  /**
   * Schedule an asynchronous copy of 2D data
   *
   * @param link    Link handling the transfer
   * @param source  Source buffer view
   * @param target  Target buffer view
   */
  def copy2D(link:Link,source:BufferView2D,target:BufferView2D):DataTransfer

  override def copy(link:Link,source:BufferView,target:BufferView):DataTransfer = {
    
    if (source.buffer.memory != link.source || target.buffer.memory != link.target)
      throw new Exception("Invalid copy: link cannot transfer between the given buffers")
    
    (source,target) match {
      case (src:BufferView2D,tgt:BufferView2D) => {
        if (src.width != tgt.width)
          throw new Exception("Invalid copy: different widths (%d,%d)".format(src.width,tgt.width))
        if (src.height != tgt.height)
          throw new Exception("Invalid copy: different heights (%d,%d)".format(src.height,tgt.height))
        val ev = copy2D(link,src,tgt)
        AsyncGC.add(ev, source, target)
        ev
      }
      case _ => super.copy(link,source,target)
    }
  }
}

/**
 * Link support for 3D copy
 */
trait Copy3DSupport extends MemoryCopier {
  /**
   * Schedule an asynchronous copy of 3D data
   *
   * @param link    Link handling the transfer
   * @param source  Source buffer view
   * @param target  Target buffer view
   */
  def copy3D(link:Link,source:BufferView3D,target:BufferView3D):DataTransfer

  override def copy(link:Link,source:BufferView,target:BufferView):DataTransfer = {
    
    if (source.buffer.memory != link.source || target.buffer.memory != link.target)
      throw new Exception("Invalid copy: link cannot transfer between the given buffers")
    
    (source,target) match {
      case (src:BufferView3D,tgt:BufferView3D) => {
        if (src.width != tgt.width)
          throw new Exception("Invalid copy: different widths (%d,%d)".format(src.width,tgt.width))
        if (src.height != tgt.height)
          throw new Exception("Invalid copy: different heights (%d,%d)".format(src.height,tgt.height))
        if (src.depth != tgt.depth)
          throw new Exception("Invalid copy: different depths (%d,%d)".format(src.depth,tgt.depth))
        val ev = copy3D(link,src,tgt)
        AsyncGC.add(ev, source, target)
        ev
      }
      case _ => super.copy(link,source,target)
    }
  }
}
