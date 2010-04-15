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
import scala.collection.immutable.BitSet.BitSet1
import java.nio.ByteBuffer

class Buffer(val context:Context, val peer:Pointer) extends Mem

object Buffer {
   import Wrapper._
   import OpenCL.checkError
   import Mem._

   def fromByteBuffer(context:Context, flags:Long, bb:ByteBuffer): Buffer = {
      val err = new IntByReference
      val peer = clCreateBuffer(context.peer, flags, bb.capacity, bb, err.getPointer)
      checkError(err.getValue)
      new Buffer(context, peer)
   }

   def allocate(context:Context, flags:Long, size:Long): Buffer = {
      val err = new IntByReference
      val peer = clCreateBuffer(context.peer, flags, size, null, err.getPointer)
      checkError(err.getValue)
      new Buffer(context, peer)
   }

   def cloneByteBuffer(context:Context, flags:Long, bb:ByteBuffer): Buffer = {
      val err = new IntByReference
      val peer = clCreateBuffer(context.peer, flags, bb.capacity, bb, err.getPointer)
      checkError(err.getValue)
      new Buffer(context, peer)
   }
}
