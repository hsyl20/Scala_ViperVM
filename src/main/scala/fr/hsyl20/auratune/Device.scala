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

import scala.collection.immutable._
import fr.hsyl20.{opencl => cl}

class Device(val peer: cl.Device) {
   var inactiveBuffers: List[Buffer] = Nil
   var activeBuffers: List[Buffer] = Nil

   val context = new cl.Context(peer)

   //TODO: enable profiling only if necessary
   //TODO: enable outOfOrder only if supported
   private val cq = new cl.CommandQueue(context, peer, outOfOrder=true, profiling=true)

   def execute(t:Task, after:Seq[Event]): Event = 
      new Event(cq.enqueueKernel(t.codelet.kernel(context), t.globalWorkSize, t.localWorkSize, after.map(_.peer)))
}

object Device {
   lazy val list = for (p <- cl.OpenCL.platforms ; d <- p.devices()) yield new Device(d)
}