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

import org.vipervm.platform.{Processor,MemoryNode}
import org.vipervm.utils._

/**
 * An engine execute a given functional program
 */
class Engine(proc:Processor,mem:MemoryNode) {

  def evaluate(expr:Term, context:Context):TmData = expr match {
    case TmData(name)   => TmData(name)
    case TmApp(k@TmKernel(_),args)  => submit(k, args.par.map(x => evaluate(x,context)).seq, context)
    case _ => ???
  }

  var id = 0

  def submit(kernel:TmKernel, args:Vector[TmData], context:Context):TmData = {

    val d = synchronized {
      val r = TmData("d"+id)
      id += 1
      r
    }
    
    println("%s <- %s(%s)".format(d.name, kernel.name, args.map(_.name).mkString(",")))

    val ker = context.kernels(kernel.name).getKernelsFor(proc).head
    val datas = args.map(_.name).map(context.datas(_)).flatMap(_.viewIn(mem))
  
    //TODO
//    proc.execute(ker, datas

    d
  }
}

sealed abstract class Term
case class TmData(name:String) extends Term
case class TmKernel(name:String) extends Term
case class TmApp(kernel:TmKernel, args:Vector[Term]) extends Term

case class Context(datas:Map[String,Data], kernels:Map[String,MetaKernel])
