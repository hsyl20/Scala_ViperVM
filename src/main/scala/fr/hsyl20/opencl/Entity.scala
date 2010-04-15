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
import scala.collection.immutable.BitSet.BitSet1

trait Entity {
   val peer: Pointer
}

trait Info {
   import OpenCL.checkError

   protected val infoFunc: (Int, Int, Pointer,Pointer) => Int

   private def getInfoMem(info:Int): (Memory,Int) = {
      val num = new IntByReference
      checkError(infoFunc(info, 0, NULL, num.getPointer))
      val mem = new Memory(num.getValue)
      checkError(infoFunc(info, num.getValue, mem, NULL))
      (mem, num.getValue)
   }

   private def getInfo(info:Int): Memory = getInfoMem(info)._1

   protected def getStringInfo(info:Int): String = getInfo(info).getString(0,false)

   protected def getIntInfo(info:Int): Int = getInfo(info).getInt(0)

   protected def getBoolInfo(info:Int): Boolean = getInfo(info).getInt(0) != 0

   protected def getLongInfo(info:Int): Long = getInfo(info).getLong(0)

   protected def getBitSetInfo(info:Int): BitSet1 = new BitSet1(getLongInfo(info))

   protected def getLongArrayInfo(info:Int): Seq[Long] = {
      val (m,n) = getInfoMem(info)
      m.getLongArray(0,n / 8).toList
   }

   protected def getNativeSizeInfo(info:Int): Long = {
      val m = getInfo(info)
      if (NativeLong.SIZE == 8)
         m.getLong(0)
      else
         m.getInt(0).toLong
   }

   protected def getPointerInfo(info:Int): Pointer = {
      val m = getInfo(info)
      if (Pointer.SIZE == 8)
         new Pointer(m.getLong(0))
      else
         new Pointer(m.getInt(0).toLong)
   }

   protected def getNativeSizeArrayInfo(info:Int): Seq[Long] = {
      val (m,n) = getInfoMem(info)
      if (NativeLong.SIZE == 8)
         m.getLongArray(0, n/8).toList
      else
         m.getIntArray(0, n/4).toList.map(_.toLong)
   }
}

trait Retainable extends Entity {
   import OpenCL.checkError

   protected val retainFunc: (Pointer) => Int
   protected val releaseFunc: (Pointer) => Int

   def retain: Unit = checkError(retainFunc(peer))
   def release: Unit = checkError(releaseFunc(peer))

   override protected def finalize: Unit = {
      try {
         release
      } catch {
         case e:OpenCLException => ()
      }
   }
}
