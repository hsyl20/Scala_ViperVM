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

package fr.hsyl20.auratune.runtime.opencl

import fr.hsyl20.{opencl => cl}


/**
 * Program is a wrapper for cl_program.
 * 
 * It stores a list of built programs for different devices
 */
class Program(val source:String) {

  private var peers: Map[OpenCLDevice,cl.Program] = Map.empty
  private var compatibleDevices: Map[OpenCLDevice,Boolean] = Map.empty

  /**
   * Indicate whether the program can be executed on the given device (None if we don't know)
   *
   * Without any further information, we can only know it after trying
   * to compile it for the device.
   * Inherited classes overload this method to enhance this
   */
  def isCompatibleWith(device:OpenCLDevice): Option[Boolean] = compatibleDevices.get(device)


  /**
   * Try to compile the program for the given device.
   */
  def compileFor(device:OpenCLDevice): cl.Program = {
    val t = isCompatibleWith(device).getOrElse(true)
    if (!t)
      error("This program isn't compatible with the specified device")

    try {
      val p = new cl.Program(device.context, source)
      p.build(List(device.peer))
      peers += (device -> p)
      compatibleDevices += (device -> true)
      p
    } catch {
      case e => compatibleDevices += (device -> false) ; throw e
    }
  }

  /**
   * Return the compiled program for the device
   */
  def get(device:OpenCLDevice): cl.Program = peers.get(device) match {
    case Some(p) => p
    case None => compileFor(device)
  }

}
