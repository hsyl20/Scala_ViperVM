/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.platform.opencl

import com.sun.jna.Pointer
import com.sun.jna.ptr._

import fr.hsyl20.vipervm.platform._
import fr.hsyl20.{opencl => cl}

/**
 * An OpenCL kernel
 */
abstract class OpenCLKernel extends Kernel {

  /* We use implicit conversions to cast from generic types
   * to backend types. We can do this because it is the
   * of the responsability of the scheduler to provide backends
   * with appropriate device and buffer types.
   */
  private implicit def dev2dev(d:Processor): OpenCLDevice =
    d.asInstanceOf[OpenCLDevice]

  private implicit def buf2buf(b:Buffer): OpenCLBuffer =
    b.asInstanceOf[OpenCLBuffer]

  private var peers: Map[OpenCLDevice,cl.Kernel] = Map.empty

  /** Source program */
  val program: OpenCLProgram
  /** Kernel name in the program */
  val name:String

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
   * Indicate if the kernel can be executed on the given processor.
   * 
   * Default behavior is to valid any OpenCLDevice. Kernels should overwrite
   * this to set more precise constraints (double support, etc.)
   */
  def canExecuteOn(proc:Processor): Boolean = proc match {
    case _:OpenCLDevice => true
    case _ => false
  }

  /**
   * Retrieve kernel configuration from parameters. Concrete kernels must implement this
   */
  def configure(device:OpenCLDevice, params:Seq[KernelParameter]): Option[OpenCLKernelConfig]
}

/**
 * Configuration for a kernel execution
 */
abstract class OpenCLKernelConfig {

  /**
   * Global work size
   */
  val globalWorkSize: List[Long]

  /**
   * Local work size (optional)
   *
   * Default behavior is to let the OpenCL implementation decide how
   * to break the global work-items into work-groups
   */
  val localWorkSize: Option[List[Long]] = None

  /**
   * Effective kernel parameters
   *
   * These parameters can include shared memory allocation, etc.
   */
  val parameters: IndexedSeq[KernelParameter]
}
