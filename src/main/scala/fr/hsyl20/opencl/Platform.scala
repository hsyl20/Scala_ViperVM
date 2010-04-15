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
import com.sun.jna.ptr.{IntByReference, PointerByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeLong, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._

/**
 * An OpenCL platform
 */
class Platform(val peer:Pointer) extends Entity with Info {
   import Wrapper._
   import Platform._
   import OpenCL.checkError

   protected val infoFunc = clGetPlatformInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer) 

   lazy val profile: String = getStringInfo(CL_PLATFORM_PROFILE)
   lazy val version: String = getStringInfo(CL_PLATFORM_VERSION)
   lazy val name: String = getStringInfo(CL_PLATFORM_NAME)
   lazy val vendor: String = getStringInfo(CL_PLATFORM_VENDOR)
   lazy val extensions: Seq[String] = getStringInfo(CL_PLATFORM_EXTENSIONS).split(' ').toList

   def devices(deviceType: Int = Device.CL_DEVICE_TYPE_ALL): Seq[Device] = {
      val num = new IntByReference
      checkError(clGetDeviceIDs(peer, deviceType, 0, NULL, num.getPointer))
      
      val mem = new Memory(Pointer.SIZE * num.getValue)
      checkError(clGetDeviceIDs(peer, deviceType, num.getValue, mem, NULL))

      mem.getPointerArray(0, num.getValue).toList.map(new Device(this, _))
   }
}

object Platform {
   val CL_PLATFORM_PROFILE     =   0x0900
   val CL_PLATFORM_VERSION     =   0x0901
   val CL_PLATFORM_NAME        =   0x0902
   val CL_PLATFORM_VENDOR      =   0x0903
   val CL_PLATFORM_EXTENSIONS  =   0x0904
}
