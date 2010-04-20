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

class Data(val hostBuffer:ByteBuffer) {
   var buffers: Map[Device, Buffer] = Map.empty

   val size = hostBuffer.capacity

   def synchronize(buffer:Buffer, access:Argument.AccessMode): Event = {
      import Buffer.State._
      import Argument.AccessMode._
      //TODO
      (buffer.state,access) match {
         case (Invalid, In) => //perform copy ()
      }

      new Event(null)
   }
}
