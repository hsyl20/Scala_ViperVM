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

package org.vipervm.bindings.opencl

import net.java.dev.sna.SNA
import com.sun.jna.ptr.{IntByReference, PointerByReference, LongByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeSize, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._
import scala.collection.immutable.BitSet.BitSet1
import java.nio.ByteBuffer

class Buffer(val context:Context, val peer:Pointer) extends Mem

object Buffer {
   import Wrapper._
   import OpenCL.checkError
   import Mem._
   import Mem.AccessMode._

   def create(context:Context, size:Long, access:AccessMode = ReadWrite): Buffer = {
      val err = new IntByReference
      val flags = access.id
      val peer = clCreateBuffer(context.peer, flags, size, null, err.getPointer)
      checkError(err.getValue)
      new Buffer(context, peer)
   }

   def fromByteBuffer(context:Context, bb:ByteBuffer, access:AccessMode = ReadWrite): Buffer = {
      if (context.endianness != bb.order)
         throw new Exception("Cannot use this ByteBuffer with this context (wrong endianness)")
      val err = new IntByReference
      val flags = CL_MEM_USE_HOST_PTR | access.id
      val peer = clCreateBuffer(context.peer, flags, bb.capacity, bb, err.getPointer)
      checkError(err.getValue)
      new Buffer(context, peer)
   }

   def allocate(context:Context, size:Int, access:AccessMode = ReadWrite): Buffer = {
      val bb = ByteBuffer.allocateDirect(size)
      bb.order(context.endianness)
      fromByteBuffer(context, bb, access)
   }

}
