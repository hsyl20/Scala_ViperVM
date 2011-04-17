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

package fr.hsyl20.opencl

import net.java.dev.sna.SNA
import com.sun.jna.ptr.{IntByReference, PointerByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeLong, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._

class Event(val commandQueue:CommandQueue, val peer:Pointer) extends Entity with Retainable with Info {
   import Wrapper._
   import Event._

   protected val retainFunc = clRetainEvent    
   protected val releaseFunc = clReleaseEvent
   protected val infoFunc = clGetEventInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer)

   def referenceCount  = getIntInfo(CL_EVENT_REFERENCE_COUNT)
   def commandType     = CommandType(getIntInfo(CL_EVENT_COMMAND_TYPE))
   def executionStatus = ExecutionStatus(getIntInfo(CL_EVENT_COMMAND_EXECUTION_STATUS))

   val profilingInfo = new ProfilingInfo(this)

   def syncWait:Unit = Event.waitFor(this)
}


object Event {
   import OpenCL.checkError
   import Wrapper._

   val CL_EVENT_COMMAND_QUEUE                   =   0x11D0
   val CL_EVENT_COMMAND_TYPE                    =   0x11D1
   val CL_EVENT_REFERENCE_COUNT                 =   0x11D2
   val CL_EVENT_COMMAND_EXECUTION_STATUS        =   0x11D3

   object ExecutionStatus extends Enumeration {
      val CL_COMPLETE  =   0x0
      val CL_RUNNING   =   0x1
      val CL_SUBMITTED =   0x2
      val CL_QUEUED    =   0x3

      val Complete = Value(CL_COMPLETE)
      val Running = Value(CL_RUNNING)
      val Submitted = Value(CL_SUBMITTED)
      val Queued = Value(CL_QUEUED)
   }

   object CommandType extends Enumeration {
      val CL_COMMAND_NDRANGE_KERNEL        =   0x11F0
      val CL_COMMAND_TASK                  =   0x11F1
      val CL_COMMAND_NATIVE_KERNEL         =   0x11F2
      val CL_COMMAND_READ_BUFFER           =   0x11F3
      val CL_COMMAND_WRITE_BUFFER          =   0x11F4
      val CL_COMMAND_COPY_BUFFER           =   0x11F5
      val CL_COMMAND_READ_IMAGE            =   0x11F6
      val CL_COMMAND_WRITE_IMAGE           =   0x11F7
      val CL_COMMAND_COPY_IMAGE            =   0x11F8
      val CL_COMMAND_COPY_IMAGE_TO_BUFFER  =   0x11F9
      val CL_COMMAND_COPY_BUFFER_TO_IMAGE  =   0x11FA
      val CL_COMMAND_MAP_BUFFER            =   0x11FB
      val CL_COMMAND_MAP_IMAGE             =   0x11FC
      val CL_COMMAND_UNMAP_MEM_OBJECT      =   0x11FD
      val CL_COMMAND_MARKER                =   0x11FE
      val CL_COMMAND_ACQUIRE_GL_OBJECTS    =   0x11FF
      val CL_COMMAND_RELEASE_GL_OBJECTS    =   0x1200

      val NDRangeKernel = Value(CL_COMMAND_NDRANGE_KERNEL)
      val Task = Value(CL_COMMAND_TASK)
      val NativeKernel = Value(CL_COMMAND_NATIVE_KERNEL)
      val ReadBuffer = Value(CL_COMMAND_READ_BUFFER)
      val WriteBuffer = Value(CL_COMMAND_WRITE_BUFFER)
      val CopyBuffer = Value(CL_COMMAND_COPY_BUFFER)
      val ReadImage = Value(CL_COMMAND_READ_IMAGE)
      val WriteImage = Value(CL_COMMAND_WRITE_IMAGE)
      val CopyImage = Value(CL_COMMAND_COPY_IMAGE)
      val CopyImageToBuffer = Value(CL_COMMAND_COPY_IMAGE_TO_BUFFER)
      val CopyBufferToImage = Value(CL_COMMAND_COPY_BUFFER_TO_IMAGE)
      val MapBuffer = Value(CL_COMMAND_MAP_BUFFER)
      val MapImage = Value(CL_COMMAND_MAP_IMAGE)
      val UnMapMemObject = Value(CL_COMMAND_UNMAP_MEM_OBJECT)
      val Marker = Value(CL_COMMAND_MARKER)
      val AcquireGLObjects = Value(CL_COMMAND_ACQUIRE_GL_OBJECTS)
      val ReleaseGLObjects = Value(CL_COMMAND_RELEASE_GL_OBJECTS)
   }

   def waitFor(events:Seq[Event]): Unit = {
      checkError(clWaitForEvents(events.size, events.map(_.peer).toArray))
   }

   def waitFor(event:Event): Unit = waitFor(IndexedSeq(event))
}

class ProfilingInfo(event:Event) extends Info {
   import ProfilingInfo._
   import Wrapper._

   protected val infoFunc = clGetEventProfilingInfo(event.peer, _:Int, _:Int, _:Pointer, _:Pointer)
   
   def queued: Long = getLongInfo(CL_PROFILING_COMMAND_QUEUED)
   def submit: Long = getLongInfo(CL_PROFILING_COMMAND_SUBMIT)
   def start: Long = getLongInfo(CL_PROFILING_COMMAND_START)
   def end: Long = getLongInfo(CL_PROFILING_COMMAND_END)
}

object ProfilingInfo {
   val CL_PROFILING_COMMAND_QUEUED              =   0x1280
   val CL_PROFILING_COMMAND_SUBMIT              =   0x1281
   val CL_PROFILING_COMMAND_START               =   0x1282
   val CL_PROFILING_COMMAND_END                 =   0x1283
}
