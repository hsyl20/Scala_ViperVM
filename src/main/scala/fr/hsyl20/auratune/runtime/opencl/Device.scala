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

import fr.hsyl20.auratune.runtime._
import fr.hsyl20.{opencl => cl}

import com.sun.jna.Pointer
import com.sun.jna.ptr._

/* OpenCL device */
class OpenCLDevice(val peer:cl.Device) extends Device {

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

  /**
   * Device memory
   */
  val memoryNodes = List(new OpenCLMemoryNode(this))

  /**
   * Execute the kernel with the specified parameters
   */
  def execute(configuredKernel:ConfiguredKernel): RunningKernel = {
    val kernel = configuredKernel.kernel
    val args = configuredKernel.args

    /* Get compiled kernel for this device */
    val k = kernel.get(this)

    /* Get work sizes */
    val global = kernel.computeGlobalWorkSize(this, args)
    val local = kernel.computeLocalWorkSize(this, args)

    /* Get parameter list */
    val params = kernel.computeParameters(this, args)

    /* We need to synchronize as OpenCL kernels are not thread safe */
    k.synchronized {

      /* Set parameters */
      for ((a,idx) <- params.zipWithIndex) a match {
        case BufferKernelParameter(b) => k.setArg(idx, Pointer.SIZE, b.peer.peer)
        case IntKernelParameter(v) => k.setArg(idx, 4, new IntByReference(v).getPointer)
        case DoubleKernelParameter(v) => k.setArg(idx, 8, new DoubleByReference(v).getPointer)
        case FloatKernelParameter(v) => k.setArg(idx, 4, new FloatByReference(v).getPointer)
      }

      /* Enqueue kernel */
      val e = commandQueue.enqueueKernel(k, global, local, null)

      RunningKernel(configuredKernel, this, new OpenCLEvent(e))
    }
  }

  /**
   * Test if the kernel can be executed with the given parameters
   * Return a list of errors or Nil if none
   */
  //TODO
  def canExecute(configuredKernel:ConfiguredKernel): Boolean = true
}
