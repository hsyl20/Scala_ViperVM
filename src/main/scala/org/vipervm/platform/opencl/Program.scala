/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.platform.opencl

import org.vipervm.bindings.opencl._


/**
 * Program is a wrapper for cl_program.
 * 
 * It stores a list of built programs for different devices
 */
class OpenCLProgram(val source:String) {

  private var peers: Map[OpenCLProcessor,Program] = Map.empty
  private var compatibleDevices: Map[OpenCLProcessor,Boolean] = Map.empty

  /**
   * Indicate whether the program can be executed on the given device (None if we don't know)
   *
   * Without any further information, we can only know it after trying
   * to compile it for the device.
   * Inherited classes overload this method to enhance this
   */
  def isCompatibleWith(device:OpenCLProcessor): Option[Boolean] = compatibleDevices.get(device)


  /**
   * Try to compile the program for the given device.
   * TODO: program should be compiled once for every possible target
   */
  def compileFor(device:OpenCLProcessor): Program = {
    val t = isCompatibleWith(device).getOrElse(true)
    if (!t)
      sys.error("This program isn't compatible with the specified device")

    try {
      val p = new Program(device.context, source)
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
  def get(device:OpenCLProcessor): Program = peers.get(device) match {
    case Some(p) => p
    case None => compileFor(device)
  }

}
