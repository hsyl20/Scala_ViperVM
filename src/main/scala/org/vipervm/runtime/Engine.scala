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

  def evaluate(expr:Term, context:Context):FutureValue = expr match {
    case TmVar(name)   => context.values(name)
    case TmApp(TmKernel(name),args)  => {
      val k = context.kernels(name)
      val params = args.par.map(x => evaluate(x,context)).seq
      submit(k, params, context)
    }
    case _ => ???
  }

  private def submit(fkernel:FunctionalKernel, params:Vector[FutureValue], context:Context):FutureValue = {

    val ftask = fkernel.createTask(params)
    //We explicitly wait for task creation (even if it requires access to param
    //values)
    val task = ftask()
    val result = task.result
    val deps = params

    val event = scheduler.submitTask(task,deps)
    
    new FutureValue(result,event)
  }
}

sealed abstract class Term
case class TmVar(name:String) extends Term
case class TmKernel(name:String) extends Term
case class TmApp(kernel:TmKernel, args:Vector[Term]) extends Term

case class Context(values:Map[String,FutureValue], kernels:Map[String,FunctionalKernel])


