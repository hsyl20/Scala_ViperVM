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
**
*/

package fr.hsyl20.auratune

import fr.hsyl20.{opencl => cl}


/**
 * Program is a wrapper for cl_program.
 * 
 * It stores a list of built programs for different devices
 */
class Program(val source:String) {

   private var peers: Map[Device,cl.Program] = Map.empty

   def get(device:Device): cl.Program = peers.get(device) match {
      case Some(p) => p
      case None => {
         val p = new cl.Program(device.context, source)
         p.build(List(device.peer))
         peers += (device -> p)
         p
      }
   }
}
