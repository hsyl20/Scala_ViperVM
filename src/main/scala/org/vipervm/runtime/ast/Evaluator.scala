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

package org.vipervm.runtime.ast

import org.vipervm.runtime.Data

/**
 * Evaluate a functional task graph (sequential)
 */
class Evaluator {
  def evalc(term:Term):Term = term match {

    case DataTerm(_) => term

    case AppTerm(KernelTerm(k),as) => {
      val eas = evalc(as)
      if (!isData(eas)) throw new InvalidProgram("Evaluation of kernel parameter to something other than a data")
      val data = asData(eas)

      //FIXME: multi-arg kernels
      val task = k.createTask(List(data))

      //TODO
      println("Scheduling execution %s = %s(%s)".format(task.output,k,task.input.mkString(",")))

      DataTerm(task.output)
    }
  }

  /** Multi-step evaluator */
  def eval(ctx:Context,t:Term):Term = try {
    val t2  = eval1(ctx,t)
    eval(ctx,t2)
  } catch {
    case _ => t
  }

  def eval1(ctx:Context,t:Term):Term = t match {
    case AppTerm(AbsTerm(t12,x),v2) if isVal(ctx,v2) => t12.substTop(v2)
    case AppTerm(v1,t2) if isVal(ctx,v1) => {
      val t2b = eval1(ctx,t2)
      AppTerm(v1,t2b)
    }
    case AppTerm(t1,t2) => {
      val t1b =  eval1(ctx,t1)
      AppTerm(t1b,t2)
    }
    case _ => throw NoRuleApplies
  }

  private def isData(term:Term):Boolean = term match {
    case DataTerm(_) => true
    case _ => false
  }

  private def asData(term:Term):Data = term match {
    case DataTerm(d) => d
    case _ => throw new Exception("Invalid conversion: term isn't a data")
  }

  private def isVal(ctx:Context,t:Term):Boolean = t match {
    case AbsTerm(_,_) => true
    case _ => false
  }
}


class InvalidProgram(str:String) extends Exception(str)
object NoRuleApplies extends Exception
