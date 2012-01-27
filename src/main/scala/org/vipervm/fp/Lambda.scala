package org.vipervm.fp

sealed abstract class Term

case class TmVar(n:Int,totalLength:Int) extends Term
case class TmAbs(name:String,term:Term) extends Term
case class TmApp(t1:Term,t2:Term) extends Term
case object TmTrue extends Term
case object TmFalse extends Term
case class TmIf(t1:Term,t2:Term,t3:Term) extends Term
case class TmLet(name:String,t1:Term,t2:Term) extends Term
case class TmData(id:Int) extends Term
case class TmKernel(name:String,arity:Int) extends Term
case class TmPartialApp(kernel:TmKernel, args:Seq[Term]) extends Term


object Term {
  def tmMapVar(onvar:(Int,TmVar)=>Term,t:Term,c:Int=0):Term = t match {
    case v@TmVar(_,_) => onvar(c,v)
    case TmAbs(x,t1) => TmAbs(x, tmMapVar(onvar,t1,c+1))
    case TmApp(t1,t2) => TmApp(tmMapVar(onvar,t1,c), tmMapVar(onvar,t2,c))
    case TmTrue => TmTrue
    case TmFalse => TmFalse
    case TmIf(t1,t2,t3) => TmIf(tmMapVar(onvar,t1,c),tmMapVar(onvar,t2,c),tmMapVar(onvar,t3,c))
    case TmLet(name,t1,t2) => TmLet(name,tmMapVar(onvar,t1,c),tmMapVar(onvar,t2,c+1))
    case TmKernel(name,arity) => TmKernel(name,arity)
    case TmPartialApp(kernel, args) => TmPartialApp(kernel, args.map(x => tmMapVar(onvar,x,c)))
    case TmData(id) => TmData(id)
  }

  def shift(step:Int,t:Term):Term = {
    def onvar(c:Int,v:TmVar):Term = {
      val TmVar(x,n) = v
      if (x>c) TmVar(x+step,n+step) else TmVar(x,n+step)
    }
    tmMapVar(onvar,t)
  }

  def subst(j:Int,s:Term,t:Term):Term = {
    def onvar(c:Int,v:TmVar):Term = {
      val TmVar(x,n) = v
      if (x==j+c) shift(c,s) else v
    }
    tmMapVar(onvar,t)
  }

  def substTop(s:Term,t:Term):Term = {
    shift(-1, subst(0, shift(1,s), t))
  }

  def isValue(ctx:Context,t:Term):Boolean = t match {
    case TmAbs(_,_) => true
    case TmTrue => true
    case TmFalse => true
    case TmKernel(_,_) => true
    case TmData(_) => true
    case _ => false
  }

  case object NoRuleApplies extends Exception

  /**
   * Single step evaluation
   */
  def eval(ctx:Context,t:Term):Term = try {
    eval(ctx, eval1(ctx,t))
  }
  catch {
    case NoRuleApplies => t
  }

  def eval1(ctx:Context,t:Term):Term = t match {
    case TmApp(TmAbs(x,t12),v2) if isValue(ctx,v2) => substTop(v2,t12)
    case TmApp(k@TmKernel(_,_),t2) => TmPartialApp(k, Seq(eval(ctx,t2)))
    case TmApp(v1,t2) if isValue(ctx,v1) => TmApp(v1,eval1(ctx,t2))
    case TmApp(TmPartialApp(k,as),t2) => TmPartialApp(k, as :+ eval(ctx,t2))
    case TmApp(t1,t2) => TmApp(eval1(ctx,t1), t2)
    case TmIf(TmTrue,t2,_) => t2
    case TmIf(TmFalse,_,t3) => t3
    case TmIf(t1,t2,t3) => TmIf(eval1(ctx,t1), t2, t3)
    case TmLet(_,v1,t2) if isValue(ctx, v1) => substTop(v1,t2)
    case TmLet(name,t1,t2) => TmLet(name, eval1(ctx,t1), t2)
    case TmPartialApp(k,args) if args.length == k.arity => submit(k,args)
    case _ => throw NoRuleApplies
  }

  var ids = 0

  def submit(kernel:TmKernel, args:Seq[Term]):TmData = {
    val as = args.map { _ match {
      case TmData(id) => "Data(%d)".format(id)
      case e@_ => throw new Exception("Invalid arg: "+e)
    }}
    val ret = TmData(ids)
    println("Data(%d) <- %s(%s)".format(ids, kernel.name, as.mkString(",")))
    ids += 1
    ret
  }

}


class Binding

class Context(items:Vector[(String,Binding)])  {
  def pickFreshName(x:String):(Context,String) = {
    if (!items.map(_._1).contains(x)) {
      (new Context(items :+ (x,new Binding)), x)
    }
    else pickFreshName(x+"_")
  }

  def length = items.length

  def apply(x:Int):String = items(x)._1

  def this() = this(Vector.empty)
}
