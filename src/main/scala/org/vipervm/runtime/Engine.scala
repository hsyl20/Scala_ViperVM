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

import grizzled.slf4j.Logging
import org.vipervm.platform.{Event,FutureEvent}
import org.vipervm.utils._
import org.vipervm.runtime.scheduling.Scheduler

import scala.collection.mutable

/**
 * An engine execute a given functional program
 */
class Engine(scheduler:Scheduler) {

  val events:mutable.Map[Value,Event] = mutable.Map.empty

  def evaluate(expr:Term,context:Context):FutureEvent[Value] = {
    val d = eval(expr,context)
    val ev = events(d)
    new FutureEvent(d,ev)
  }

  private def eval(expr:Term, context:Context):Value = expr match {
    case TmVar(name)   => context.values(name)
    case TmApp(TmKernel(name),args)  => {
      val k = context.kernels(name)
      //val params = args.par.map(x => eval(x,context)).seq
      val params = args.map(x => eval(x,context))
      submit(k, params, context)
    }
    case _ => ???
  }

  private def submit(fkernel:FunctionalKernel, args:Vector[Value], context:Context):Value = {

    val params = args.map( _ match {
      case DataValue(v) => DataTaskParameter(v)
      case IntValue(v) => IntTaskParameter(v)
      case FloatValue(v) => FloatTaskParameter(v)
      case DoubleValue(v) => DoubleTaskParameter(v)
    })
    val (task,returned) = fkernel.createTask(params)
    val deps = args.flatMap(events.get(_))

    val result = DataValue(returned)
    val ev = scheduler.submitTask(task,deps)
    
    events += (result -> ev)

    result
  }
}

sealed abstract class Term
case class TmVar(name:String) extends Term
case class TmKernel(name:String) extends Term
case class TmApp(kernel:TmKernel, args:Vector[Term]) extends Term

case class Context(values:Map[String,Value], kernels:Map[String,FunctionalKernel])

