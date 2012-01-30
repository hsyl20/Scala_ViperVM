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

import org.vipervm.platform.{Event,FutureEvent}
import org.vipervm.utils._
import org.vipervm.runtime.scheduling.Scheduler

import scala.collection.mutable

/**
 * An engine execute a given functional program
 */
class Engine(scheduler:Scheduler) {

  val events:mutable.Map[Data,Event] = mutable.Map.empty

  def evaluate(expr:Term,context:Context):FutureEvent[Data] = {
    val d = eval(expr,context)
    val ev = events(d)
    new FutureEvent(ev,d)
  }

  private def eval(expr:Term, context:Context):Data = expr match {
    case TmData(name)   => context.datas(name)
    case TmApp(TmKernel(name),args)  => {
      val k = context.kernels(name)
      //val params = args.par.map(x => eval(x,context)).seq
      val params = args.map(x => eval(x,context))
      submit(k, params, context)
    }
    case _ => ???
  }

  private def submit(fkernel:FunctionalKernel, args:Vector[Data], context:Context):Data = {

    val params = args.map(DataTaskParameter(_))
    val (task,result) = fkernel.createTask(params)
    val deps = args.flatMap(events.get(_))
    
    val ev = scheduler.submitTask(task,deps)
    
    events += (result -> ev)

    result
  }
}

sealed abstract class Term
case class TmData(name:String) extends Term
case class TmKernel(name:String) extends Term
case class TmApp(kernel:TmKernel, args:Vector[Term]) extends Term

case class Context(datas:Map[String,Data], kernels:Map[String,FunctionalKernel])
