package org.vipervm.fp

/** Lambda calculus */
sealed abstract class LambdaTerm

case class TmVar(info:Info,n:Int,totalLength:Int) extends LambdaTerm
case class TmAbs(info:Info,name:String,term:LambdaTerm) extends LambdaTerm
case class TmApp(info:Info,t1:LambdaTerm,t2:LambdaTerm) extends LambdaTerm

object LambdaTerm {
  def tmMap(onvar:(Int,TmVar)=>LambdaTerm,t:LambdaTerm,c:Int=0):LambdaTerm = t match {
    case v@TmVar(_,_,_) => onvar(c,v)
    case TmAbs(fi,x,t1) => TmAbs(fi,x, tmMap(onvar,t1,c+1))
    case TmApp(fi,t1,t2) => TmApp(fi, tmMap(onvar,t1,c), tmMap(onvar,t2,c))
  }

  def shift(t:LambdaTerm,step:Int):LambdaTerm = {
    def onvar(c:Int,v:TmVar):LambdaTerm = v match {
      case TmVar(fi,x,n) => if (x>c) TmVar(fi,x+step,n+step) else TmVar(fi,x,n+step)
    }
    tmMap(onvar,t)
  }

  def subst(j:Int,s:LambdaTerm,t:LambdaTerm):LambdaTerm = {
    def onvar(c:Int,v:TmVar):LambdaTerm = v match {
      case TmVar(fi,x,n) => if (x==j+c) shift(s,c) else TmVar(fi,x,n)
    }
    tmMap(onvar,t)
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

  def apply(fi:Info,x:Int):String = items(x)._1
}

object LambdaPrinter {

  def print(ctx:Context,t:LambdaTerm):String = t match {
    case TmAbs(fi,x,t1) => {
      val (ctx2,x2) = ctx.pickFreshName(x)
      "(lambda %s. %s )".format(x2, print(ctx2,t1))
    }
    case TmApp(fi,t1,t2) => "(%s %s)".format(print(ctx,t1),print(ctx,t2))
    case TmVar(fi,x,n) => {
      if (ctx.length != n) throw new Exception("Bad index")
      ctx(fi,x)
    }
  }
}
