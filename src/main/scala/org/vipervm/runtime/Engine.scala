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

import org.vipervm.platform.Platform
import org.vipervm.platform.opencl.OpenCLProcessor
import org.vipervm.utils._

/**
 * An engine execute a given functional program
 */
class Engine(platform:Platform) {

  val device = platform.processors.filter(_.isInstanceOf[OpenCLProcessor]).headOption
  val proc = device.getOrElse {
    throw new Exception("No OpenCL device available")
  }

  val mem = proc.memory


  def evaluate(expr:Term, context:Context):TmData = expr match {
    case TmData(name)   => TmData(name)
    case TmApp(k@TmKernel(_),args)  => submit(k, args.par.map(x => evaluate(x,context)).seq, context)
    case _ => ???
  }

  var id = 0

  def submit(kernel:TmKernel, args:Vector[TmData], context:Context):TmData = {
    context.kernels(kernel.name)

    val d = synchronized {
      val r = TmData("d"+id)
      id += 1
      r
    }
    
    println("%s <- %s(%s)".format(d.name, kernel.name, args.map(_.name).mkString(",")))
    d
  }
}

sealed abstract class Term
case class TmData(name:String) extends Term
case class TmKernel(name:String) extends Term
case class TmApp(kernel:TmKernel, args:Vector[Term]) extends Term

case class Context(datas:Map[String,Data], kernels:Map[String,MetaKernel])
