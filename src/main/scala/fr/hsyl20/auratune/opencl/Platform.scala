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

package fr.hsyl20.auratune.opencl

import fr.hsyl20.{opencl => cl}

class Device(val peer: cl.Device) {

   var buffers: List[Buffer] = Nil

   val context = new cl.Context(peer)

   val cq = {
      val ooo = peer.queueOutOfOrderSupport
      //TODO: enable profiling only if necessary
      val prof = peer.queueProfilingSupport
      new cl.CommandQueue(context, peer, outOfOrder=ooo, profiling=prof)
   }

   /* Execute a task, assuming all data are present in device memory */
   def execute(t:Task): Event = {

      /* Lock data in memory */
      for ((s,d) <- t.inputs) {
         if (!d.lockInDevice(this))
            throw new Exception("Task input data not present in device memory")
      }
      for ((s,d) <- t.outputs) {
         if (!d.lockInDevice(this))
            throw new Exception("Task input data not present in device memory")
      }

      t.enqueue(this)
   }
}

object Platform {
   private lazy val deviceList = for (p <- cl.OpenCL.platforms ; d <- p.devices()) yield new Device(d)

   def devices:Seq[Device] = deviceList
}
