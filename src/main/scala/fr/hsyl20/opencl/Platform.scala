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
      val err = clGetDeviceIDs(peer, deviceType, 0, NULL, num.getPointer)
      if (err != OpenCLException.CL_DEVICE_NOT_FOUND && err != OpenCLException.CL_SUCCESS)
        checkError(err)

      if (num.getValue != 0) {
        val mem = new Memory(Pointer.SIZE * num.getValue)
        checkError(clGetDeviceIDs(peer, deviceType, num.getValue, mem, NULL))

        mem.getPointerArray(0, num.getValue).toList.map(new Device(this, _))
      }
      else Nil
   }
}

object Platform {
   val CL_PLATFORM_PROFILE     =   0x0900
   val CL_PLATFORM_VERSION     =   0x0901
   val CL_PLATFORM_NAME        =   0x0902
   val CL_PLATFORM_VENDOR      =   0x0903
   val CL_PLATFORM_EXTENSIONS  =   0x0904
}
