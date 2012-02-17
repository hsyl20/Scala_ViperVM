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

import org.vipervm.platform._
import org.vipervm.bindings.{opencl => cl}

import com.sun.jna.Pointer
import com.sun.jna.ptr._

/* OpenCL device */
class OpenCLProcessor(val peer:cl.Device) extends Processor {

  type MemoryNodeType = OpenCLMemoryNode

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

  val memories = Seq(new OpenCLMemoryNode(this))

  /**
   * Compile the kernel for this processor
   */
  def compile(kernel:Kernel):Unit = {
    kernel.get(this)
  }

  /**
   * Execute the kernel with the specified parameters
   */
  def execute(kernel:Kernel, args:Seq[Any]): KernelEvent = {

    /* Check params */
    val config = kernel.configure(this, args) match {
      case None => throw new Exception("Invalid kernel parameters")
      case Some(p) => p
    }

    /* Get compiled kernel for this device */
    val k = kernel.get(this)

    /* We need to synchronize as OpenCL kernels are not thread safe */
    val e = k.synchronized {

      /* Set parameters */
      for ((a,idx) <- config.parameters.zipWithIndex) a match {
        case OpenCLBufferKernelParameter(b) => k.setArg(idx, Pointer.SIZE, new PointerByReference(b.peer.peer).getPointer)
        case OpenCLIntKernelParameter(v) => k.setArg(idx, 4, new IntByReference(v).getPointer)
        case OpenCLLongKernelParameter(v) => k.setArg(idx, 8, new LongByReference(v).getPointer)
        case OpenCLDoubleKernelParameter(v) => k.setArg(idx, 8, new DoubleByReference(v).getPointer)
        case OpenCLFloatKernelParameter(v) => k.setArg(idx, 4, new FloatByReference(v).getPointer)
      }

      /* Enqueue kernel */
      commandQueue.enqueueKernel(k, config.globalWorkSize, config.localWorkSize, Nil)
    }

    /* Avoid entity GC until kernel completion */
    val ev = new OpenCLEvent(e)
    AsyncGC.add(ev, config.parameters)

    new KernelEvent(kernel, args, this, ev)
  }

  override def toString = "OpenCL: %s - %s".format(peer.vendor, peer.name)

  override def equals(a:Any):Boolean = a match {
    case a:OpenCLProcessor if a.peer == peer => true
    case _ => false
  }
}
