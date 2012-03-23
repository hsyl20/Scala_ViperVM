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

package org.vipervm.runtime

import org.vipervm.platform._
import org.vipervm.runtime._
import org.vipervm.runtime.mm.config._
import org.vipervm.runtime.mm.{Data,DataInstance}

/**
 * Wrapper that makes a kernel functional
 */
trait MetaKernel {

  val prototype:FunctionPrototype

  /** Low-level kernel */
  val kernel:Kernel

  /** Low-level kernel prototype */
  val kernelPrototype:List[Parameter[_]]

  /** Make low-level kernel parameters from data instances */
  def makeKernelParams(params:Seq[DataInstance]):Seq[Any]

  /** Indicate if this kernel can be executed on the given processor */
  def canExecuteOn(proc:Processor):Boolean

  def memoryConfig(params:Seq[Data],memory:MemoryNode,hostMemory:MemoryNode):DataConfig = {
    if (params.length != kernelPrototype.length) throw new Exception("Invalid parameters")
    val paramMem = params zip kernelPrototype.map(_.storage match {
      case HostStorage => hostMemory
      case DeviceStorage => memory
    })
    val constraints = paramMem map { case (p,m) => p requiredIn m }
    constraints.reduceLeft[DataConfig](_&&_)
  }

  protected implicit def instanceExtractor(params:Seq[DataInstance]) = new {
    def apply[A : Manifest](param:Parameter[A]) = try {
      params(kernelPrototype.indexOf(param)).asInstanceOf[A]
    }
    catch {
      case e => throw new Exception("Invalid parameter for \"%s\" at position %d. You should pass a %s that corresponds to the following description: %s)".format(param.name, kernelPrototype.indexOf(param), manifest[A].toString, param.description))
    }
  }

}
