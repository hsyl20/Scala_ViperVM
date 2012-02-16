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

package org.vipervm.runtime.interpreter

import org.vipervm.platform.{Event,FutureEvent}
import org.vipervm.runtime.FunctionalKernel
import org.vipervm.runtime.scheduling.Scheduler
import org.vipervm.utils._

import scala.collection.mutable

/**
 * Interpreter for functional programs
 */
class Interpreter(scheduler:Scheduler) {

  def evaluate(expr:Term, symbols:SymbolTable):FutureValue = expr match {
    case TmVar(name)   => symbols.values(name)
    case TmApp(TmKernel(name),args)  => {
      val k = symbols.kernels(name)
      val params = args.par.map(x => evaluate(x,symbols)).seq
      submit(k, params, symbols)
    }
    case _ => ???
  }

  private def submit(fkernel:FunctionalKernel, params:Vector[FutureValue], symbols:SymbolTable):FutureValue = {

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



