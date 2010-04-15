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
**
*/

package fr.hsyl20.opencl

import net.java.dev.sna.SNA
import com.sun.jna.ptr.{IntByReference, PointerByReference, LongByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeSize, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._

class Program(val context:Context, val source:String) extends Entity with Retainable with Info {
   import OpenCL.checkError
   import Wrapper._
   import Program._
   
   protected val retainFunc = clRetainProgram
   protected val releaseFunc = clReleaseProgram
   protected val infoFunc = clGetProgramInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer)


   private val err = new IntByReference
   val peer = clCreateProgramWithSource(context.peer, 1, List(source).toArray, List(NativeSize(source.length)).toArray, err.getPointer)
   checkError(err.getValue)

   def build(devices:Seq[Device], options:String = ""): Unit = {
      val err = clBuildProgram(peer, devices.size, devices.map(_.peer).toArray, options, NULL,NULL)
      checkError(err)
      //TODO: add built devices to a list
   }

   def referenceCount: Int = getIntInfo(CL_PROGRAM_REFERENCE_COUNT)
   def numDevices: Int = getIntInfo(CL_PROGRAM_NUM_DEVICES)
   //FIXME: we don't provide devices
   //FIXME: we don't provide program binaries

   def buildInfo(device:Device): BuildInfo = new BuildInfo(this, device)

   def createKernels: Seq[Kernel] = {
      import Kernel._
      val num = new IntByReference
      checkError(clCreateKernelsInProgram(peer, 0, NULL, num.getPointer))

      val mem = new Memory(Pointer.SIZE * num.getValue)
      checkError(clCreateKernelsInProgram(peer, num.getValue, mem, NULL))

      mem.getPointerArray(0, num.getValue).toList.map(k => {
         val num = new IntByReference
         checkError(clGetKernelInfo(k, CL_KERNEL_FUNCTION_NAME, 0, NULL, num.getPointer))
         val mem = new Memory(num.getValue)
         checkError(clGetKernelInfo(k, CL_KERNEL_FUNCTION_NAME, num.getValue, mem, NULL))
         new Kernel(this, mem.getString(0,false), k)
      })
   }
}


object Program {
   val CL_PROGRAM_REFERENCE_COUNT               =   0x1160
   val CL_PROGRAM_CONTEXT                       =   0x1161
   val CL_PROGRAM_NUM_DEVICES                   =   0x1162
   val CL_PROGRAM_DEVICES                       =   0x1163
   val CL_PROGRAM_SOURCE                        =   0x1164
   val CL_PROGRAM_BINARY_SIZES                  =   0x1165
   val CL_PROGRAM_BINARIES                      =   0x1166
}

class BuildInfo(program:Program, device:Device) extends Info {
   import BuildInfo._
   import Wrapper._

   protected val infoFunc = clGetProgramBuildInfo(program.peer, device.peer, _:Int, _:Int, _:Pointer, _:Pointer)
   
   def status: Int = getIntInfo(CL_PROGRAM_BUILD_STATUS)
   def options: String = getStringInfo(CL_PROGRAM_BUILD_OPTIONS)
   def log: String = getStringInfo(CL_PROGRAM_BUILD_LOG)
}

object BuildInfo {
   val CL_BUILD_SUCCESS                         =   0
   val CL_BUILD_NONE                            =   -1
   val CL_BUILD_ERROR                           =   -2
   val CL_BUILD_IN_PROGRESS                     =   -3

   val CL_PROGRAM_BUILD_STATUS                  =   0x1181
   val CL_PROGRAM_BUILD_OPTIONS                 =   0x1182
   val CL_PROGRAM_BUILD_LOG                     =   0x1183
}
