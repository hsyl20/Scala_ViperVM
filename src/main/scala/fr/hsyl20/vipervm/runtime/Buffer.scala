/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.runtime

import com.sun.jna.Memory
import scala.collection.immutable.NumericRange

import scala.concurrent.Lock

/**
 * A buffer in a memory node
 */
abstract class Buffer {
  val memoryNode:MemoryNode

  private var _kernels:List[RunningKernel] = Nil
  private val kernelsLock:Lock = new Lock

  private var _transfers:List[DataTransfer] = Nil
  private val transfersLock:Lock = new Lock

  /** Free this buffer */
  def free: Unit

  /** Driver specific 1D copy implementation */
  protected def driverCopyFromHost(src:Memory,size:Long,srcOffset:Long,dstOffset:Long): Event

  protected def driverCopyToHost(dst:Memory,size:Long,srcOffset:Long=0,dstOffset:Long=0): Event


  /** Copy data from host */
  final def copyFromHost(src:Memory,size:Long,srcOffset:Long=0,dstOffset:Long=0): DataTransfer1D = {

    val event = driverCopyFromHost(src,size,srcOffset,dstOffset)

    DataTransfer1D(HostToMemoryNode,dstOffset, src, srcOffset, event,
      size)
  }


  /**
   * Copy 2D data from host
   *
   * Default behavior is to launch a copy for each row
   */
  final def copyFromHost2D(src:Memory,elemSize:Int,width:Long,height:Long,
    srcPadding:Long, dstPadding:Long, srcOffset:Long, dstOffset:Long): DataTransfer2D = {

    val event = driverCopyFromHost2D(src,elemSize,width,height,srcPadding,dstPadding,srcOffset,dstOffset)

    DataTransfer2D(HostToMemoryNode, dstOffset, src, srcOffset, event,
      elemSize, width, height, dstPadding, srcPadding)
  }

  protected def driverCopyFromHost2D(src:Memory,elemSize:Int,width:Long,height:Long,
    srcPadding:Long, dstPadding:Long, srcOffset:Long, dstOffset:Long): Event = {

    copyGeneric2D(driverCopyFromHost)(src,elemSize,width,height,srcPadding,dstPadding,srcOffset,dstOffset)
  }

  /**
   * Copy 3D data from host
   *
   * Default behavior is to launch a 2D copy for each plane
   */
  final def copyFromHost3D(src:Memory,elemSize:Int,width:Long,height:Long,depth:Long,
  srcRowPadding:Long, srcPlanePadding:Long, dstRowPadding:Long, dstPlanePadding:Long,
  srcOffset:Long,dstOffset:Long): DataTransfer3D = {

    val event = driverCopyFromHost3D(src, elemSize, width, height, depth,
    srcRowPadding, srcPlanePadding, dstRowPadding, dstPlanePadding,
    srcOffset, dstOffset)

    DataTransfer3D(HostToMemoryNode, dstOffset, src, srcOffset, event,
      elemSize, width, height, depth,
      dstRowPadding, dstPlanePadding, srcRowPadding, srcPlanePadding)
  }

  protected def driverCopyFromHost3D(src:Memory,elemSize:Int,width:Long,height:Long,depth:Long,
  srcRowPadding:Long, srcPlanePadding:Long, dstRowPadding:Long, dstPlanePadding:Long,
  srcOffset:Long,dstOffset:Long): Event = {

    copyGeneric3D(driverCopyFromHost2D)(src,elemSize,width,height,depth,srcRowPadding,srcPlanePadding,dstRowPadding,dstPlanePadding,srcOffset,dstOffset)

  }

  /**
   * Copy data to host
   */
  final def copyToHost(dst:Memory,size:Long,srcOffset:Long=0,dstOffset:Long=0): DataTransfer1D = {

    val event = driverCopyToHost(dst,size,srcOffset,dstOffset)

    DataTransfer1D(MemoryNodeToHost,srcOffset,dst,dstOffset,event,size)
  }


  /**
   * Copy 2D data to host
   *
   * Default behavior is to launch a copy for each row
   */
  final def copyToHost2D(dst:Memory,elemSize:Int,width:Long,height:Long,
    srcPadding:Long, dstPadding:Long, srcOffset:Long,dstOffset:Long): DataTransfer2D = {

    val event = driverCopyToHost2D(dst, elemSize, width, height,
      srcPadding, dstPadding, srcOffset, dstOffset)

    DataTransfer2D(MemoryNodeToHost, srcOffset, dst, dstOffset, event,
      elemSize, width, height, srcPadding, dstPadding)
  }

  protected def driverCopyToHost2D(dst:Memory,elemSize:Int,width:Long,height:Long,
    srcPadding:Long, dstPadding:Long, srcOffset:Long,dstOffset:Long): Event = {

    copyGeneric2D(driverCopyToHost)(dst,elemSize,width,height,srcPadding,dstPadding,srcOffset,dstOffset)
  }

  /**
   * Copy 3D data to host
   *
   * Default behavior is to launch a 2D copy for each plane
   */
  final def copyToHost3D(dst:Memory,elemSize:Int,width:Long,height:Long,depth:Long,
  srcRowPadding:Long, srcPlanePadding:Long, dstRowPadding:Long, dstPlanePadding:Long,
  srcOffset:Long,dstOffset:Long): DataTransfer3D = {

    val event = driverCopyToHost3D(dst, elemSize, width, height, depth,
      srcRowPadding, srcPlanePadding, dstRowPadding, dstPlanePadding,
      srcOffset, dstOffset)

    DataTransfer3D(MemoryNodeToHost, srcOffset, dst, dstOffset, event,
      elemSize, width, height, depth, srcRowPadding, srcPlanePadding,
      dstRowPadding, dstPlanePadding)
  }

  protected def driverCopyToHost3D(dst:Memory,elemSize:Int,width:Long,height:Long,depth:Long,
  srcRowPadding:Long, srcPlanePadding:Long, dstRowPadding:Long, dstPlanePadding:Long,
  srcOffset:Long,dstOffset:Long): Event = {

    copyGeneric3D(driverCopyToHost2D)(dst,elemSize,width,height,depth,srcRowPadding,srcPlanePadding,dstRowPadding,dstPlanePadding,srcOffset,dstOffset)

  }

  /**
   * Generic 2D copy
   */
  protected def copyGeneric2D[FromTo](copy1D:(FromTo,Long,Long,Long)=>Event)(srcDst:FromTo,elemSize:Int,width:Long,height:Long, srcPadding:Long, dstPadding:Long, srcOffset:Long,dstOffset:Long): Event = {

    if (srcPadding == dstPadding) {
      copy1D(srcDst,(elemSize*width+srcPadding)*height,srcOffset,dstOffset)
    } else {
      val srcRowSize = elemSize*width + srcPadding
      val dstRowSize = elemSize*width + dstPadding
      val evs = for (r <- NumericRange(0L, height, 1L)) yield {
        copy1D(srcDst,
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
  protected def copyGeneric3D[FromTo](copy2D:(FromTo,Int,Long,Long,Long,Long,Long,Long)=>Event)(srcDst:FromTo,elemSize:Int,width:Long,height:Long,depth:Long,
  srcRowPadding:Long, srcPlanePadding:Long, dstRowPadding:Long, dstPlanePadding:Long, srcOffset:Long,dstOffset:Long): Event = {
    
    if (srcRowPadding == dstRowPadding) {
      val size = (width+srcRowPadding)*height
      copy2D(srcDst,elemSize,size,depth,srcPlanePadding,dstPlanePadding,srcOffset,dstOffset)
    } else {
      val srcPlaneSize = (elemSize*width+srcRowPadding)*height+srcPlanePadding
      val dstPlaneSize = (elemSize*width+dstRowPadding)*height+dstPlanePadding
      val evs = for (r <- NumericRange(0L, depth,1L)) yield {
        copy2D(srcDst,
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

  def srcTransfers: List[DataTransfer] =
    transfers.filter(_.direction == MemoryNodeToHost)

  def dstTransfers: List[DataTransfer] =
    transfers.filter(_.direction == HostToMemoryNode)

  def transfers: List[DataTransfer] = _transfers

  protected def startTransfer(t:DataTransfer): Unit = transfersLock.synchronized {
    _transfers ::= t
  }

  protected def endTransfer(t:DataTransfer): Unit = transfersLock.synchronized {
    _transfers = _transfers.filterNot(_ == t)
  }


  def kernels: List[RunningKernel] = _kernels

  def startKernelAccess(k:RunningKernel) = kernelsLock.synchronized {
    _kernels ::= k
  }

  def endKernelAccess(k:RunningKernel) = kernelsLock.synchronized {
    _kernels = _kernels.filterNot(_ == k)
  }
}
