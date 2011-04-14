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

import fr.hsyl20.vipervm.platform._
import fr.hsyl20.{opencl => cl}

import com.sun.jna.Pointer
import com.sun.jna.ptr._

/* OpenCL device */
class OpenCLProcessor(val peer:cl.Device) extends Processor {

  implicit def ker2ker(k:Kernel):OpenCLKernel =
    k.asInstanceOf[OpenCLKernel]

  private implicit def buf2buf(b:Buffer): OpenCLBuffer =
    b.asInstanceOf[OpenCLBuffer]

   
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

  /** Device memory */
  val memory = new OpenCLMemoryNode(this)

  val memories = Seq(memory)

  /**
   * Execute the kernel with the specified parameters
   */
  def execute(kernel:Kernel, args:Seq[KernelParameter]): KernelEvent = {

    /* Check params */
    val config = kernel.configure(this, args) match {
      case None => throw new Exception("Invalid kernel parameters")
      case Some(p) => p
    }

    /* Get compiled kernel for this device */
    val k = kernel.get(this)

    /* We need to synchronize as OpenCL kernels are not thread safe */
    k.synchronized {

      /* Set parameters */
      for ((a,idx) <- config.parameters.zipWithIndex) a match {
        case BufferKernelParameter(b) => k.setArg(idx, Pointer.SIZE, b.peer.peer)
        case IntKernelParameter(v) => k.setArg(idx, 4, new IntByReference(v).getPointer)
        case LongKernelParameter(v) => k.setArg(idx, 8, new LongByReference(v).getPointer)
        case DoubleKernelParameter(v) => k.setArg(idx, 8, new DoubleByReference(v).getPointer)
        case FloatKernelParameter(v) => k.setArg(idx, 4, new FloatByReference(v).getPointer)
      }

      /* Enqueue kernel */
      val e = commandQueue.enqueueKernel(k, config.globalWorkSize, config.localWorkSize, Nil)

      new KernelEvent(kernel, args, this, new OpenCLEvent(e))
    }
  }

  override def toString = "%s - %s".format(peer.vendor, peer.name)
}
