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

import com.sun.jna.Pointer
import com.sun.jna.ptr._

import fr.hsyl20.auratune.runtime._
import fr.hsyl20.{opencl => cl}

/**
 * An OpenCL kernel
 */
abstract class OpenCLKernel(program:Program, name:String) extends Kernel {

  /* We use implicit conversions to cast from generic types
   * to backend types. We can do this because it is the
   * of the responsability of the scheduler to provide backends
   * with appropriate device and buffer types.
   */
  private implicit def dev2dev(d:Device): OpenCLDevice =
    d.asInstanceOf[OpenCLDevice]

  private implicit def buf2buf(b:Buffer): OpenCLBuffer =
    b.asInstanceOf[OpenCLBuffer]

  private var peers: Map[OpenCLDevice,cl.Kernel] = Map.empty

  /**
   * Return peer kernel for the given device
   */
  def get(device:OpenCLDevice): cl.Kernel = peers.get(device) match {
    case Some(k) => k
    case None => {
      val k = new cl.Kernel(program.get(device), name)
      peers += (device -> k)
      k
    }
  }

  /**
   * Compute global work size. Concrete kernels must implement this
   */
  def computeGlobalWorkSize(device:OpenCLDevice, args:Seq[KernelParameter]):List[Long]

  /**
   * Compute local work size. Concrete kernels should implement this
   *
   * Default behavior is to let the OpenCL implementation decide how
   * to break the global work-items into work-groups
   */
  def computeLocalWorkSize(device:OpenCLDevice, args:Seq[KernelParameter]):Option[List[Long]] = None

  /**
   * Compute parameters that will be given to the real OpenCL kernel.
   *
   * Default behavior is to pass all parameters. This method may be
   * overriden by inherited classes
   */
  def computeParameters(device:OpenCLDevice, args:Seq[KernelParameter]): Seq[KernelParameter] =
    args

}
