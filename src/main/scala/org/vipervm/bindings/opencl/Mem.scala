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
import scala.collection.immutable._
import java.nio.ByteBuffer

trait Mem extends Entity with Retainable with Info {
   import Wrapper._
   import Mem._

   protected val retainFunc = clRetainMemObject _
   protected val releaseFunc = clReleaseMemObject _
   protected val infoFunc = clGetMemObjectInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer)

   lazy val memType = getIntInfo(CL_MEM_TYPE)
   lazy val flags: BitSet = getBitSetInfo(CL_MEM_FLAGS)
   lazy val size: Long = getNativeSizeInfo(CL_MEM_SIZE)
   lazy val hostPtr: Pointer = getPointerInfo(CL_MEM_HOST_PTR)
   def hostByteBuffer: ByteBuffer = hostPtr.getByteBuffer(0,size)
   def mapCount: Int = getIntInfo(CL_MEM_MAP_COUNT)
   def referenceCount: Int = getIntInfo(CL_MEM_REFERENCE_COUNT)
   val context: Context
}

object Mem {
   object AccessMode extends Enumeration {
      type AccessMode = Value

      val CL_MEM_READ_WRITE  =   (1 << 0)
      val CL_MEM_WRITE_ONLY  =   (1 << 1)
      val CL_MEM_READ_ONLY   =   (1 << 2)

      val ReadWrite = Value(CL_MEM_READ_WRITE)
      val WriteOnly = Value(CL_MEM_WRITE_ONLY)
      val ReadOnly = Value(CL_MEM_READ_ONLY)
   }

   val CL_MEM_USE_HOST_PTR     =   (1 << 3)
   val CL_MEM_ALLOC_HOST_PTR   =   (1 << 4)
   val CL_MEM_COPY_HOST_PTR    =   (1 << 5)

   val CL_MEM_TYPE             =   0x1100
   val CL_MEM_FLAGS            =   0x1101
   val CL_MEM_SIZE             =   0x1102
   val CL_MEM_HOST_PTR         =   0x1103
   val CL_MEM_MAP_COUNT        =   0x1104
   val CL_MEM_REFERENCE_COUNT  =   0x1105
   val CL_MEM_CONTEXT          =   0x1106

   val CL_MEM_OBJECT_BUFFER                     =   0x10F0
   val CL_MEM_OBJECT_IMAGE2D                    =   0x10F1
   val CL_MEM_OBJECT_IMAGE3D                    =   0x10F2
}
