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

package org.vipervm.bindings.opencl

import org.vipervm.bindings.NativeSize
import com.sun.jna.ptr.{IntByReference, PointerByReference, LongByReference}
import com.sun.jna.{Pointer, Structure, PointerType, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable.BitSet.BitSet1
import scala.collection.immutable.BitSet

class CommandQueue(val context:Context, val device:Device, prop: BitSet1) extends Entity with Retainable with Info {
   import Wrapper._
   import CommandQueue._
   import OpenCL.{checkError, CL_FALSE, CL_TRUE}

   protected val retainFunc = clRetainCommandQueue _
   protected val releaseFunc = clReleaseCommandQueue _
   protected val infoFunc = clGetCommandQueueInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer)

   val err = new IntByReference
   val peer = clCreateCommandQueue(context.peer, device.peer, prop.elems, err.getPointer)
   checkError(err.getValue)

   def this(context:Context, device:Device, outOfOrder:Boolean = false, profiling:Boolean = false) = {
      this(context, device, new BitSet1(
            (if (outOfOrder) CommandQueue.CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE else 0L)
          + (if (profiling) CommandQueue.CL_QUEUE_PROFILING_ENABLE else 0L)
      ))
   }

   def referenceCount: Int = getIntInfo(CL_QUEUE_REFERENCE_COUNT)
   def properties: BitSet = getBitSetInfo(CL_QUEUE_PROPERTIES)

   def setProperties(prop: BitSet, enable: Boolean): BitSet1 = {
      val num = new LongByReference
      val flag = if (enable) CL_TRUE else CL_FALSE
      val err = clSetCommandQueueProperty(peer, prop.sum, flag, num.getPointer)
      checkError(err)
      new BitSet.BitSet1(num.getValue)
   }

   def disableProperties(prop: BitSet): BitSet = setProperties(prop, false)
   def enableProperties(prop: BitSet): BitSet = setProperties(prop, true)

   def flush = checkError(clFlush(peer))
   def finish = checkError(clFinish(peer))

   private def eventWaitList(events:Seq[Event]): Seq[Pointer] = 
      if (events.size != 0) events.map(_.peer) else null

   def enqueueReadBuffer(buffer:Buffer, blocking:Boolean, offset:Long, size:Long, ptr:Pointer, events:Seq[Event]): Event = {
      val b = if (blocking) CL_TRUE else CL_FALSE
      val ev = new PointerByReference
      checkError(clEnqueueReadBuffer(peer, buffer.peer, b, offset, size, ptr, events.size, eventWaitList(events), ev.getPointer))
      new Event(this, ev.getValue)
   }

   def enqueueWriteBuffer(buffer:Buffer, blocking:Boolean, offset:Long, size:Long, ptr:Pointer, events:Seq[Event]): Event = {
      val b = if (blocking) CL_TRUE else CL_FALSE
      val ev = new PointerByReference
      checkError(clEnqueueWriteBuffer(peer, buffer.peer, b, offset, size, ptr, events.size, eventWaitList(events), ev.getPointer))
      new Event(this, ev.getValue)
   }

   def enqueueWriteBuffer(buffer:Buffer, blocking:Boolean, offset:Long, mem:Memory, events:Seq[Event]): Event =
      enqueueWriteBuffer(buffer, blocking, offset, mem.size, mem.getPointer(0), events)

   def enqueueCopyBuffer(src:Buffer, dst:Buffer, srcOffset:Long, dstOffset:Long, size:Long, events:Seq[Event]): Event = {
      val ev = new PointerByReference
      checkError(clEnqueueCopyBuffer(peer, src.peer, dst.peer, srcOffset, dstOffset, size, events.size, eventWaitList(events), ev.getPointer))
      new Event(this, ev.getValue)
   }

   def enqueueMapBuffer(buffer:Buffer, blocking:Boolean, flags:Long, offset:Long, size:Long, events:Seq[Event]): (Pointer,Event) = {
      val ev = new PointerByReference
      val err = new IntByReference
      val b = if (blocking) CL_TRUE else CL_FALSE
      val map = clEnqueueMapBuffer(peer, buffer.peer, b, flags, offset, size, events.size, eventWaitList(events), ev.getPointer, err.getPointer)
      checkError(err.getValue)
      (map, new Event(this, ev.getValue))
   }

   def enqueueUnmap(mem:Mem, ptr:Pointer, events:Seq[Event]): Event = {
      val ev = new PointerByReference
      checkError(clEnqueueUnmapMemObject(peer, mem.peer, ptr, events.size, eventWaitList(events), ev.getPointer))
      new Event(this, ev.getValue)
   }

   def enqueueKernel(kernel:Kernel, globalWorkSize:Seq[Long], localWorkSize:Option[Seq[Long]], events:Seq[Event]): Event = {
      val ev = new PointerByReference
      val gws = globalWorkSize.map(new NativeSize(_))
      val lws = localWorkSize.map(_.map(new NativeSize(_))).getOrElse(null)
      val err = clEnqueueNDRangeKernel(peer, kernel.peer, gws.size, null, gws, lws, events.size, eventWaitList(events), ev.getPointer)
      checkError(err)
      new Event(this, ev.getValue)
   }

   def enqueueTask(kernel:Kernel, events:Seq[Event]): Event = {
      val ev = new PointerByReference
      checkError(clEnqueueTask(peer, kernel.peer, events.size, eventWaitList(events), ev.getPointer))
      new Event(this, ev.getValue)
   }

   def enqueueWaitForEvents(events:Seq[Event]): Unit = {
      checkError(clEnqueueWaitForEvents(peer, events.size, eventWaitList(events)))
   }

   def enqueueMarker: Event = {
      val ev = new PointerByReference
      checkError(clEnqueueMarker(peer, ev.getPointer))
      new Event(this, ev.getValue)
   }

   def enqueueBarrier: Unit = checkError(clEnqueueBarrier(peer))
}

object CommandQueue {
   val CL_QUEUE_OUT_OF_ORDER_EXEC_MODE_ENABLE   =   (1L << 0)
   val CL_QUEUE_PROFILING_ENABLE                =   (1L << 1)

   val CL_QUEUE_CONTEXT                         =   0x1090
   val CL_QUEUE_DEVICE                          =   0x1091
   val CL_QUEUE_REFERENCE_COUNT                 =   0x1092
   val CL_QUEUE_PROPERTIES                      =   0x1093

   val CL_MAP_READ                              =   (1 << 0)
   val CL_MAP_WRITE                             =   (1 << 1)
}
