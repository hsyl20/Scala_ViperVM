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

import grizzled.slf4j.Logging
import com.sun.jna.ptr.{IntByReference, PointerByReference}
import com.sun.jna.{Pointer, Structure, PointerType, NativeLong, Memory}
import com.sun.jna.Pointer.NULL
import scala.collection.immutable._

object OpenCL extends Logging {
   import Wrapper._

  /* Enable protected mode */
  com.sun.jna.Native.setProtected(true)
  if (com.sun.jna.Native.isProtected)
    info("JNA protected mode: enabled")
  else 
    info("JNA protected mode: disabled")

  /* Check for libjsig.so */
  val checkjsig = Option(System.getenv().get("LD_PRELOAD")).map(_.contains("libjsig")).getOrElse(false)
  if (!checkjsig)
    warn("If you encounter JVM crash, use the following command:\n  export LD_PRELOAD=/path/to/your/jvm/lib/yourarch/libjsig.so")


   val CL_FALSE = 0
   val CL_TRUE  = 1

   def checkError(err:Int): Unit = {
      if (err != OpenCLException.CL_SUCCESS)
         throw new OpenCLException(err)
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

