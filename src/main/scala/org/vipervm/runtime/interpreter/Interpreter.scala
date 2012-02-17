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

import org.vipervm.platform.{Event,FutureEvent,FutureData}
import org.vipervm.runtime.{Function,Task}
import org.vipervm.runtime.scheduling.Scheduler
import org.vipervm.utils._

import scala.collection.mutable

/**
 * Interpreter for functional programs
 */
class Interpreter(scheduler:Scheduler) {

  def evaluate(program:Program):FutureData = evaluate(program.term,program.symbols)

  def evaluate(expr:Term, symbols:SymbolTable):FutureData = expr match {
    case TmVar(name)   => symbols.values(name)
    case TmApp(TmKernel(name),args)  => {
      val k = symbols.functions(name)
      val params = args.par.map(x => evaluate(x,symbols)).seq
      submit(k, params, symbols)
    }
    case _ => ???
  }

  private def submit(function:Function, params:Vector[FutureData], symbols:SymbolTable):FutureData = {

    val ftask = function.createTask(params)
    //We explicitly wait for task creation (even if it requires access to param
    //values)
    val task = ftask()
    val result = task.result
    val deps = params

    val event = submitTask(task,deps)
    
    new FutureData(result,event)
  }

  protected def submitTask(task:Task,deps:Seq[Event]):Event = {
    scheduler.submitTask(task,deps)
  }
}



