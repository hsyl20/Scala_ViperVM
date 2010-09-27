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

class Buffer(val data:Data, val device:Device) {
   val peer = device.context.createBuffer(data.size)

   var ready = false
   var refcount = 0

   /**
    * Try to detach this buffer from its original Data.
    * Attach it to a new data and mark it as not ready.
    *
    * Detached buffers can be used as ReadWrite kernel arguments
    */
   def detach: Data = Data.fromBuffer(this)

   def retain: Unit  = { refcount = refcount + 1 }
   def release: Unit = { refcount = refcount - 1 }
}
