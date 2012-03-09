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
import org.vipervm.runtime.{Function,Task}
import org.vipervm.runtime.scheduling.Scheduler
import org.vipervm.runtime.mm.{Data,DataManager}
import org.vipervm.library.Library
import org.vipervm.utils._


/**
 * Interpreter for functional programs
 */
class Interpreter(scheduler:Scheduler,library:Library,dataManager:DataManager) {

  protected def mapVar(t:Term,cutoff:Int=0)(f:(Int,TmVar)=>Term) :Term = t match {
    case v@TmVar(_,_) => f(cutoff,v)
    case t => t
  }
  
  protected def shift(step:Int,t:Term):Term = mapVar(t) { (c,v) => {
    val TmVar(x,n) = v
    if (x>c) TmVar(x+step,n+step) else TmVar(x,n+step)
  }}

  protected def subst(j:Int,s:Term,t:Term):Term = mapVar(t) { (c,v) => {
    val TmVar(x,n) = v
    if (x==j+c) shift(c,s) else v
  }}

  protected def substTop(s:Term,t:Term):Term = {
    shift(-1, subst(0, shift(1,s), t))
  }

  def print(context:Context,term:Term):String = term match {
    case TmAbs(x,t1) => {
      val (ctx2,name) = context.pickFreshName(x)
      "(ƛ %s. %s)".format(name, print(ctx2, t1))
    }
    case TmApp(t1,ts) => {
      val pts = ts.map(t => print(context,t)).mkString(" ")
      "(%s %s)".format(print(context,t1),pts)
    }
    case TmVar(x,n) if context.length == n => context.indexToName(x)
    case TmVar(_,_) => "[Invalid index]"
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

      /* Select functions with valid name */
      val fn0 = library.byName(name)
      if (fn0.isEmpty) throw new Exception("Unknown operation %s".format(name))

      /* Filter functions with valid types */
      val paramTypes = params.map(_.typ.get)
      val fn1 = fn0.filter(_.prototype.resultType(paramTypes).isDefined)
      if (fn1.isEmpty) throw new Exception("Not function %s found with valid parameter types".format(name))

      /* Create result data */
      val resType = fn1.head.prototype.resultType(paramTypes).get
      val result = dataManager.create
      result.typ = resType

      /* Schedule task execution */
      submit(fn1, params, result)

      TmData(result)
    }

    case TmApp(v1,ts) if isValue(context, v1) => eval(context, TmApp(v1, ts.map(eval(context,_))))
    case TmApp(t1,t2) => eval(context, TmApp(eval(context,t1), t2))
    case v if isValue(context,v) => v
    case _ => throw new Exception("Interpreter unable to return a value")
  }

  def evaluate(term:Term):Data = {
    eval(new Context(Nil), term) match {
      case TmData(data) => data
      case t => throw new Exception("Result isn't a data (%s)".format(t))
    }
  }

  private def submit(function:Seq[Function], params:Seq[Data], result:Data):Unit = {

    /*val ftask = function.createTask(params)
    //We explicitly wait for task creation (even if it requires access to param
    //values)
    val task = ftask()
    val result = task.result
    val deps = params

    val event = submitTask(task,deps)
    */
  }

}

