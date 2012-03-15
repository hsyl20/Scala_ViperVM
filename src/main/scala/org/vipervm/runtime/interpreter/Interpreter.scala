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

import org.vipervm.runtime.{Function,Task,Runtime}
import org.vipervm.runtime.mm.Data
import org.vipervm.library.Library


/**
 * Interpreter for functional programs
 */
class DefaultInterpreter(runtime:Runtime) {
  protected val library = runtime.library

  /**
   * Evaluate a term and return a resulting data.
   * If the evaluation result isn't a data, and exception is thrown
   */
  def evaluate(term:Term):Data = {
    eval(new Context(Nil), term) match {
      case TmData(data) => data
      case t => throw new Exception("Result isn't a data (%s)".format(t))
    }
  }


  protected def isValue(context:Context,term:Term):Boolean = term match {
    case TmAbs(_,_)
    | TmId(_)
    |TmData(_) => true
    case _ => false
  }

  protected def eval(context:Context,term:Term):Term = term match {

    case TmApp(TmId(name),vs) if vs.forall(isValue(context,_)) => {

      val params = vs.asInstanceOf[Seq[TmData]].map(_.data)

      /* Select functions and rules with valid name */
      val fn0 = library.byName(name)
      val rules0 = library.rulesByName(name)
      if (fn0.isEmpty) throw new Exception("Unknown operation %s".format(name))

      /* Filter functions and rules with valid types */
      val paramTypes = params.map(_.typ.get)
      val fn1 = fn0.filter(_.prototype.resultType(paramTypes).isDefined)
      val rules1 = rules0.filter(_.prototype.resultType(paramTypes).isDefined)
      if (fn1.isEmpty) throw new Exception("Not function %s found with valid parameter types".format(name))

      /* Rewriting? */
      runtime.rewrite(term,rules1) match {
        case Some(t) => eval(context,t)
        case None => {

          /* Create result data */
          val resType = fn1.head.prototype.resultType(paramTypes).get
          val result = runtime.createData
          result.typ = resType

          /* Schedule task execution */
          val task = Task(fn1, params, result)

          runtime.submit(task)
          TmData(result)
        }
      }
    }

    case TmApp(v1,ts) if isValue(context, v1) => eval(context, TmApp(v1, ts.map(eval(context,_))))
    case TmApp(t1,t2) => eval(context, TmApp(eval(context,t1), t2))
    case v if isValue(context,v) => v
    case _ => throw new Exception("Interpreter unable to return a value")
  }

}

