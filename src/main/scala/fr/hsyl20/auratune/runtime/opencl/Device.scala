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

package fr.hsyl20.auratune.runtime.opencl

import fr.hsyl20.auratune.runtime.Device
import fr.hsyl20.{opencl => cl}

/* OpenCL device */
class OpenCLDevice(val peer:cl.Device) extends Device {
   
  /* We create one context per device */
  val context = new cl.Context(peer)

  /* We create one command queue per device.
   * If out-of-order mode is supported, we use it as we manage
   * dependencies ourselves.
   * Profiling is enabled if supported too */
  val commandQueue = {
    val ooo = peer.queueOutOfOrderSupport
    val prof = peer.queueProfilingSupport   //TODO: enable profiling only if necessary?
    new cl.CommandQueue(context, peer, outOfOrder=ooo, profiling=prof)
  }

  /**
   * Device memory
   */
  val memoryNodes = List(new OpenCLMemoryNode(this))
}
