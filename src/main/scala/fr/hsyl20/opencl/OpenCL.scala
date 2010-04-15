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

object OpenCL {
   import Wrapper._

   val CL_FALSE = 0
   val CL_TRUE  = 1

   def checkError(err:Int): Unit = {
      if (err != OpenCLException.CL_SUCCESS)
         throw OpenCLException(err)
   }

   /** List available OpenCL platforms */
   def platforms: Seq[Platform] = {
      val num = new IntByReference
      checkError(clGetPlatformIDs(0, NULL, num.getPointer))
      
      val mem = new Memory(Pointer.SIZE * num.getValue)
      checkError(clGetPlatformIDs(num.getValue, mem, NULL))

      mem.getPointerArray(0, num.getValue).toList.map(new Platform(_))
   }

   def unloadCompiler: Unit = {
      val err = clUnloadCompiler()
      checkError(err)
   }
}

