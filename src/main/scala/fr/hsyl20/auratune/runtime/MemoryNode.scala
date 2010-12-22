/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**                     GPLv3
*/

package fr.hsyl20.auratune.runtime

import java.nio.ByteBuffer
import scala.collection.immutable.NumericRange

/**
 * A memory node
 */
abstract class MemoryNode {

  /**
   * Natural 2D padding
   */
  protected def padding2D(elemSize:Int,width:Long,height:Long): Long = 0
  /**
   * Natural 3D padding
   */
  protected def padding3D(elemSize:Int,width:Long,height:Long,depth:Long): Long = 0

  /**
   * Allocate a buffer
   */
  def allocate(size:Long): Buffer

  /**
   * Allocate with natural 2D padding
   *
   * Default behavior is no padding
   *
   * @return (Buffer,padding)
   */
  def allocate2D(elemSize:Int,width:Long,height:Long): (Buffer,Long) = {
    (allocate(elemSize*width*height), padding2D(elemSize,width,height))
  }

  /**
   * Allocate with natural 3D padding
   *
   * Default behavior is no padding
   *
   * @return (Buffer,padding1,padding2)
   */
  def allocate3D(elemSize:Int,width:Long,height:Long,depth:Long): (Buffer,Long,Long) = {
    val pad1 = padding2D(elemSize,width,height)
    val pad2 = padding3D(elemSize,width,height,depth)
    (allocate(elemSize*width*height*depth), pad1, pad2)
  }


  /**
   * Copy data from host
   */
  def copyFromHost(src:ByteBuffer,dst:Buffer,size:Long,srcOffset:Long=0,dstOffset:Long=0): Event

  /**
   * Copy 2D data from host
   *
   * Default behavior is to launch a copy for each row
   */
  def copyFromHost2D(src:ByteBuffer,dst:Buffer,elemSize:Int,width:Long,height:Long,
    srcPadding:Long,
    dstPadding:Long = -1,
    srcOffset:Long = 0, dstOffset:Long = 0): Event = {

    val _dstPadding = if (dstPadding == -1) padding2D(elemSize,width,height) else dstPadding

    copyGeneric2D(copyFromHost)(src,dst,elemSize,width,height,srcPadding,dstPadding,srcOffset,dstOffset)
  }

  /**
   * Copy 3D data from host
   *
   * Default behavior is to launch a 2D copy for each plane
   */
  def copyFromHost3D(src:ByteBuffer,dst:Buffer,elemSize:Int,width:Long,height:Long,depth:Long,
  srcRowPadding:Long, srcPlanePadding:Long, dstRowPadding:Long = -1, dstPlanePadding:Long = -1,
  srcOffset:Long = 0,dstOffset:Long = 0): Event = {

    val _dstRowPadding = if (dstRowPadding == -1) padding2D(elemSize,width,height) else dstRowPadding
    val _dstPlanePadding = if (dstPlanePadding == -1) padding3D(elemSize,width,height,depth) else dstPlanePadding

    copyGeneric3D(copyFromHost2D _)(src,dst,elemSize,width,height,depth,srcRowPadding,srcPlanePadding,_dstRowPadding,_dstPlanePadding,srcOffset,dstOffset)

  }

  /**
   * Copy data to host
   */
  def copyToHost(src:Buffer,dst:ByteBuffer,size:Long,srcOffset:Long=0,dstOffset:Long=0): Event

  /**
   * Copy 2D data to host
   *
   * Default behavior is to launch a copy for each row
   */
  def copyToHost2D(src:Buffer,dst:ByteBuffer,elemSize:Int,width:Long,height:Long,
    srcPadding:Long, dstPadding:Long = -1, srcOffset:Long = 0,dstOffset:Long = 0): Event = {

    val _dstPadding = if (dstPadding == -1) padding2D(elemSize,width,height) else dstPadding

    copyGeneric2D[Buffer,ByteBuffer](copyToHost _)(src,dst,elemSize,width,height,srcPadding,_dstPadding,srcOffset,dstOffset)
  }



  /**
   * Generic 2D copy
   */
  protected def copyGeneric2D[From,To](copy1D:(From,To,Long,Long,Long)=>Event)(src:From,dst:To,elemSize:Int,width:Long,height:Long, srcPadding:Long, dstPadding:Long, srcOffset:Long,dstOffset:Long): Event = {

    if (srcPadding == dstPadding) {
      copy1D(src,dst,(elemSize*width+srcPadding)*height,srcOffset,dstOffset)
    } else {
      val srcRowSize = elemSize*width + srcPadding
      val dstRowSize = elemSize*width + dstPadding
      val evs = for (r <- NumericRange(0L, height, 1L)) yield {
        copy1D(src, dst,
          srcRowSize,  //size
          srcRowSize*r, //srcOffset
          dstRowSize*r //dstOffset
        )
      }
      new EventGroup(evs)
    }
  }

  /**
   * Generic 3D copy
   */
  protected def copyGeneric3D[From,To](copy2D:(From,To,Int,Long,Long,Long,Long,Long,Long)=>Event)(src:From,dst:To,elemSize:Int,width:Long,height:Long,depth:Long,
  srcRowPadding:Long, srcPlanePadding:Long, dstRowPadding:Long, dstPlanePadding:Long, srcOffset:Long,dstOffset:Long): Event = {
    
    if (srcRowPadding == dstRowPadding) {
      val size = (width+srcRowPadding)*height
      copy2D(src,dst,elemSize,size,depth,srcPlanePadding,dstPlanePadding,srcOffset,dstOffset)
    } else {
      val srcPlaneSize = (elemSize*width+srcRowPadding)*height+srcPlanePadding
      val dstPlaneSize = (elemSize*width+dstRowPadding)*height+dstPlanePadding
      val evs = for (r <- NumericRange(0L, depth,1L)) yield {
        copy2D(src, dst,
          elemSize,
          width,
          height,
          srcRowPadding,
          dstRowPadding,
          srcPlaneSize*r, //srcOffset
          dstPlaneSize*r  //dstOffset
        )
      }
      new EventGroup(evs)
    }
  }
}
