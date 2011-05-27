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
import com.sun.jna.{Pointer, Structure, PointerType, NativeLong, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._

class Sampler(val context: Context, val normCoords:Boolean, val addressingMode:Int, val filterMode:Int) extends Entity with Retainable with Info {
   import Wrapper._
   import Sampler._
   import OpenCL._

   protected val retainFunc = clRetainSampler
   protected val releaseFunc = clReleaseSampler
   protected val infoFunc = clGetSamplerInfo(peer, _:Int, _:Int, _:Pointer, _:Pointer)

   private val nc = if (normCoords) CL_TRUE else CL_FALSE
   private val err = new IntByReference
   val peer = clCreateSampler(context.peer, nc, addressingMode, filterMode, err.getPointer)
   checkError(err.getValue)

   def referenceCount = getIntInfo(CL_SAMPLER_REFERENCE_COUNT)
}

object Sampler {
   val CL_ADDRESS_NONE                          =   0x1130
   val CL_ADDRESS_CLAMP_TO_EDGE                 =   0x1131
   val CL_ADDRESS_CLAMP                         =   0x1132
   val CL_ADDRESS_REPEAT                        =   0x1133

   val CL_SAMPLER_REFERENCE_COUNT               =   0x1150
   val CL_SAMPLER_CONTEXT                       =   0x1151
   val CL_SAMPLER_NORMALIZED_COORDS             =   0x1152
   val CL_SAMPLER_ADDRESSING_MODE               =   0x1153
   val CL_SAMPLER_FILTER_MODE                   =   0x1154

   val CL_FILTER_NEAREST                        =   0x1140
   val CL_FILTER_LINEAR                         =   0x1141
}
