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
import org.vipervm.runtime.{Function,Task,FutureData}
import org.vipervm.runtime.scheduling.Scheduler
import org.vipervm.library.Library
import org.vipervm.utils._


/**
 * Interpreter for functional programs
 */
class Interpreter(scheduler:Scheduler,library:Library) {

  def evaluate(program:Program):FutureData = evaluate(program.term,program.symbols)

  def isValue(e:Term):Boolean = e match {
    case TmId(_) => true
    case _ => false
  }

  def evaluate(expr:Term, symbols:SymbolTable):FutureData = expr match {
    case TmId(name)   => symbols.values(name)
    case TmApp(TmId(name),args)  => {
      val k = library(name).getOrElse {
        throw new Exception("Unknown operation %s".format(name))
      }
      val params = args.par.map(x => evaluate(x,symbols)).seq
      submit(k, params, symbols)
    }
    case TmLet(v,e,in) => {
      val data = evaluate(e,symbols)
      val nsymbols = symbols.copy(values = symbols.values + (v.id -> data))
      evaluate(in,nsymbols)
    }
    case _ => ???
  }

  private def submit(function:Function, params:Seq[FutureData], symbols:SymbolTable):FutureData = {

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

