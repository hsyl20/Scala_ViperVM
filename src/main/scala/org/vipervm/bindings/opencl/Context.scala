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
import java.nio.ByteBuffer

class Context(devices: Seq[Device]) extends Entity with Retainable with Info {
   import Wrapper._
   import Context._
   import OpenCL.checkError
   import Mem._

   def this(device: Device) = this(List(device))

   protected val retainFunc = clRetainContext
   protected val releaseFunc = clReleaseContext
   protected val infoFunc = clGetContextInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer)
    
   //Check that all devices have the same endianness
   val endianness = devices.head.endianness
   for (d <- devices.tail)
      if (d.endianness != endianness) throw new Exception("Context cannot contain devices with different endianess")

   // Set properties (platform)
   val properties = new Memory(Pointer.SIZE * 3)
   if (Pointer.SIZE == 8) {
      properties.setLong(0, CL_CONTEXT_PLATFORM)
      properties.setPointer(8, devices.head.platform.peer)
      properties.setLong(16, 0)
   }
   else {
      properties.setInt(0, CL_CONTEXT_PLATFORM)
      properties.setPointer(4, devices.head.platform.peer)
      properties.setInt(8, 0)
   }

   // Create context    
   private val err = new IntByReference
   //FIXME: support callback?
   val peer = clCreateContext(properties, devices.size, devices.map(_.peer).toArray, NULL, NULL, err.getPointer)
   checkError(err.getValue)

   //We don't use clCreateContextFromType. I don't think we miss much
   def this(platform:Platform, deviceType:Int) = this(platform.devices(deviceType))

   def referenceCount: Int = getIntInfo(CL_CONTEXT_REFERENCE_COUNT)
   //We already have device list and properties

   def createBuffer(size:Int, access:AccessMode.AccessMode = AccessMode.ReadWrite): Buffer = Buffer.create(this, size, access)

   def useBuffer(bb:ByteBuffer, access:AccessMode.AccessMode = AccessMode.ReadWrite): Buffer = Buffer.fromByteBuffer(this, bb, access)
   
   def allocBuffer(size:Int, access:AccessMode.AccessMode = AccessMode.ReadWrite): Buffer = Buffer.allocate(this, size, access)

}

object Context {
   val CL_CONTEXT_PLATFORM                      =   0x1084
   val CL_CONTEXT_REFERENCE_COUNT               =   0x1080
   val CL_CONTEXT_DEVICES                       =   0x1081
   val CL_CONTEXT_PROPERTIES                    =   0x1082
}
