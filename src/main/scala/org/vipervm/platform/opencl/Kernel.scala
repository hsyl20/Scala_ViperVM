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

import com.sun.jna.Pointer
import com.sun.jna.ptr._

import org.vipervm.platform._
import org.vipervm.bindings.{opencl => cl}

/**
 * An OpenCL kernel
 */
abstract class OpenCLKernel extends Kernel {

  /* We use implicit conversions to cast from generic types
   * to backend types. We can do this because it is the
   * of the responsability of the scheduler to provide backends
   * with appropriate device and buffer types.
   */
  private implicit def proc2proc(d:Processor): OpenCLProcessor =
    d.asInstanceOf[OpenCLProcessor]

  private var peers: Map[OpenCLProcessor,cl.Kernel] = Map.empty

  /** Cast parameters */
  protected implicit def buf2par(value:Buffer) = OpenCLBufferKernelParameter(value.asInstanceOf[OpenCLBuffer])
  protected implicit def int2par(value:Int) = OpenCLIntKernelParameter(value)
  protected implicit def long2par(value:Long) = OpenCLLongKernelParameter(value)
  protected implicit def float2par(value:Float) = OpenCLFloatKernelParameter(value)
  protected implicit def double2par(value:Double) = OpenCLDoubleKernelParameter(value)

  /** Source program */
  val program: OpenCLProgram
  /** Kernel name in the program */
  val name:String

  /**
   * Return peer kernel for the given device
   */
  def get(device:OpenCLProcessor): cl.Kernel = peers.get(device) match {
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
   * Default behavior is to valid any OpenCLProcessor. Kernels should overwrite
   * this to set more precise constraints (double support, etc.)
   */
  def canExecuteOn(proc:Processor): Boolean = proc match {
    case _:OpenCLProcessor => true
    case _ => false
  }

  /**
   * Retrieve kernel configuration from parameters. Concrete kernels must implement this
   */
  def configure(device:OpenCLProcessor, params:Seq[Any]): Option[OpenCLKernelConfig]
}

/**
 * Configuration for a kernel execution
 *
 * @param localWorkSize Local work size is optional. Default behavior is to let the OpenCL
 * implementation decide how to break the global work-items into work-groups
 */
case class OpenCLKernelConfig(
  val parameters: IndexedSeq[OpenCLKernelParameter],
  val globalWorkSize: List[Long],
  val localWorkSize: Option[List[Long]] = None 
)
