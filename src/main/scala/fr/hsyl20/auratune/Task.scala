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

/**
 * A task ready to be executed. This class contains references to codelet arguments
 * and various parameters (group size...)
 */
case class Task(kernel:Kernel, inputs:Map[Parameter,Data], outputs:Map[Parameter,Data],
   globalWorkSize:Seq[Long], localWorkSize:Option[Seq[Long]]) {

   def enqueue(device:Device): Event = {
      val k = kernel.get(device)
      val argPos = kernel.params.zipWithIndex

      k.synchronized { /* Kernel is not thread safe */

         /* Set kernel args and execute kernel  */
         for ((arg,idx) <- argPos) {
            arg.mode match {
               case ReadOnly  => k.setArg(idx, inputs(arg).getBuffer(device).get.peer)
               case ReadWrite => k.setArg(idx, inputs(arg).getBuffer(device).get.peer)
               case WriteOnly => k.setArg(idx, outputs(arg).getBuffer(device).get.peer)
            }
         }
         new OpenCLEvent(device.cq.enqueueKernel(k, globalWorkSize, localWorkSize, null))
      }
   }

}
