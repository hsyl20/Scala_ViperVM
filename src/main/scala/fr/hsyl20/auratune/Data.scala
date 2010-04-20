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
**                     GPLv3
*/

package fr.hsyl20.auratune

import scala.actors._
import java.nio.ByteBuffer

abstract class Data {

   type BufferType <: Buffer
   var buffers: Map[Device, BufferType] = Map.empty

   /** 
    * Synchronize data on a given device for the given access mode (maybe it shouldn't be there)
    */
   def synchronize(buffer:BufferType, access:Argument.AccessMode): Event = {
      import Buffer.State._
      import Argument.AccessMode._
      //TODO
      (buffer.state,access) match {
         case (Invalid, In) => //perform copy ()
      }

      new Event(null)
   }

   def createBuffer(device:Device): BufferType
}
