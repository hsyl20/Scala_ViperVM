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

case class CopyToDevice(d:Device)

case class CopiedToDevice(d:Device)

class Data(val hostBuffer:ByteBuffer) {
   var buffers: Map[Device, Buffer] = Map.empty

   val size = hostBuffer.capacity
}
