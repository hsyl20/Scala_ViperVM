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

import net.java.dev.sna.SNA
import com.sun.jna.ptr.{IntByReference, PointerByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeSize, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._

class Kernel(val program:Program, val name:String, vpeer:Pointer = NULL) extends Entity with Retainable with Info {
   import Wrapper._
   import Kernel._
   import OpenCL.checkError

   protected val retainFunc = clRetainKernel
   protected val releaseFunc = clReleaseKernel
   protected val infoFunc = clGetKernelInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer)
    
   val peer = if (vpeer != NULL) vpeer else {
      val err = new IntByReference
      val p = clCreateKernel(program.peer, name, err.getPointer)
      checkError(err.getValue)
      p
   }

   lazy val numArgs: Int = getIntInfo(CL_KERNEL_NUM_ARGS)
   def referenceCount: Int = getIntInfo(CL_KERNEL_REFERENCE_COUNT)
   def context: Context = program.context

   def workGroupInfo(device:Device) = new WorkGroupInfo(this, device)

   def setArg(index:Int, size:Long, value:Pointer): Unit = {
      checkError(clSetKernelArg(peer, index, size, value))
   }

   def setArg(index:Int, mem:Mem): Unit = {
      val p = new PointerByReference
      p.setValue(mem.peer)
      checkError(clSetKernelArg(peer, index, Pointer.SIZE, p.getPointer))
   }
}

object Kernel {
   val CL_KERNEL_FUNCTION_NAME                  =   0x1190
   val CL_KERNEL_NUM_ARGS                       =   0x1191
   val CL_KERNEL_REFERENCE_COUNT                =   0x1192
   val CL_KERNEL_CONTEXT                        =   0x1193
   val CL_KERNEL_PROGRAM                        =   0x1194
}

class WorkGroupInfo(kernel:Kernel, device:Device) extends Info {
   import WorkGroupInfo._
   import Wrapper._

   protected val infoFunc = clGetKernelWorkGroupInfo(kernel.peer, device.peer, _:Int, _:Int, _:Pointer, _:Pointer)
   
   def size: Long = getNativeSizeInfo(CL_KERNEL_WORK_GROUP_SIZE)
   def compileSize: Seq[Long] = getNativeSizeArrayInfo(CL_KERNEL_COMPILE_WORK_GROUP_SIZE)
   def localMemSize: Long = getLongInfo(CL_KERNEL_LOCAL_MEM_SIZE)
}

object WorkGroupInfo {
   val CL_KERNEL_WORK_GROUP_SIZE                =   0x11B0
   val CL_KERNEL_COMPILE_WORK_GROUP_SIZE        =   0x11B1
   val CL_KERNEL_LOCAL_MEM_SIZE                 =   0x11B2
}
