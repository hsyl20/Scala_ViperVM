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

abstract class Buffer(device:Device, size:Int) {
   import Buffer._

   val peer = device.context.createBuffer(size)

   var state:State = State.Invalid
}

object Buffer {
   type State = State.Value

   object State extends Enumeration {
      val Modified, Exclusive, Share, Invalid = Value
   }
}
