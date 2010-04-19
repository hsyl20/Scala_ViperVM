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

import fr.hsyl20.{opencl => cl}


class Buffer(data:Data, device:Device) {
   import Buffer._

   val peer = device.context.createBuffer(data.size)

   var state:State = State.Invalid

   def synchronize(access:Argument.AccessMode): Event = {
      import Buffer.State._
      import Argument.AccessMode._
      //TODO
      (state,access) match {
         case (Invalid, In) => //perform copy ()
      }

      new Event(null)
   }
}

object Buffer {
   type State = State.Value

   object State extends Enumeration {
      val Modified, Exclusive, Share, Invalid = Value
   }
}
