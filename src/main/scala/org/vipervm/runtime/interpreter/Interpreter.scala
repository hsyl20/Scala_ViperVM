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

import org.vipervm.utils._
import org.vipervm.platform.FutureEvent
import org.vipervm.runtime.{Function,Task,Runtime,FunctionPrototype}
import org.vipervm.runtime.mm.{Data,MetaData}
import org.vipervm.library.Library


/**
 * Interpreter for functional programs
 */
class DefaultInterpreter(runtime:Runtime) {
  protected val library = runtime.library

  /**
   * Return a type checked program
   */
  def typeCheck(term:Term):TypedTerm = term match {
    case TmData(d) => TypedTerm(term,d.typ.get)
    case TmApp(TmId(f),params) => {
      val tp = params.map(typeCheck)
      val paramTypes = tp.map(_.typ)
      val proto = library.proto(f, paramTypes)
      val retTyp = proto.resultType(paramTypes).get
      TypedTerm(term, retTyp)
    }
    case _ => throw new Exception("Unable to type term %s".format(term))
  }

  /**
   * Evaluate a term and return a resulting data.
   * If the evaluation result isn't a data, and exception is thrown
   */
  def evaluate(term:Term):Data = {
    val tterm = typeCheck(term)

    eval(new Context(Nil), tterm) match {
      case TypedTerm(TmData(data),_) => data
      case t => throw new Exception("Result isn't a data (%s)".format(t))
    }
  }

  protected def eval(context:Context,tterm:TypedTerm):TypedTerm = {
    val resultType = tterm.typ

    tterm.term match {
      case TmApp(TmId(name),vs) if vs.forall(isValue(context,_)) => {

        val params = vs.asInstanceOf[Seq[TmData]].map(_.data)
        val paramTypes = params.map(_.typ.get)
        val proto = library.proto(name,paramTypes)

        /* May require data transfers (i.e. may block) */
        val resultMeta = computeMetaData(name,params,proto).apply

        /* Check if any rewrite rule applies */
        val rules = library.rulesByProto(proto)
        val rewritten = rules.flatMap(_.rewrite(tterm.term, resultType, resultMeta))

        /* Select one rewrite rule if any */
        if (!rewritten.isEmpty) {
          eval(context,typeCheck(rewritten.head))
        }
        else {
          val funcs = library.functionsByProto(proto)

          /* Create result data */
          val result = runtime.createData
          result.typ = resultType
          result.meta = resultMeta

          /* Schedule task execution */
          val task = Task(funcs, params, result)
          runtime.submit(task)

          TypedTerm(TmData(result), resultType)
        }
      }

      case TmApp(v1,ts) if isValue(context, v1) => {
        eval(context, TypedTerm(TmApp(v1, ts.map(eval(context,_))),resultType))
      }

      case TmApp(t1,t2) => {
        eval(context, TypedTerm(TmApp(eval(context,t1), t2), resultType))
      }

      case v if isValue(context,v) => tterm

      case _ => throw new Exception("Don't know how to eval %s".format(tterm))
    }
  }

  protected def isValue(context:Context,term:Term):Boolean = term match {
    case TmId(_)
    |TmData(_) => true
    case _ => false
  }

  /**
   * Compute meta data for the given application
   */
  protected def computeMetaData(func:String, params:Seq[Data],proto:FunctionPrototype):FutureEvent[MetaData] = {
    val conf = proto.metaConf(params)

//    withConfig(conf) { proto.meta(params) }
    ???
  }

/*  protected def withConfig[A](config:DataConfig)(body: =>A):FutureEvent[A] = {
    //TODO
    ???
  }*/



}

