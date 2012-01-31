package org.vipervm.fp

object Printer {

  def needWrap(t:Term):Boolean = t match {
    case TmTrue => false
    case TmFalse => false
    case _ => true
  }

  def wrap(t:Term,ctx:Context):String = {
    if (!needWrap(t)) print(t,ctx) else "("+print(t,ctx)+")"
  }

  def print(t:Term,ctx:Context):String = t match {
    case TmTrue => "true"
    case TmFalse => "false"
    case TmIf(t1,t2,t3) => {
      "if "+wrap(t1,ctx)+" then "+wrap(t2,ctx)+" else "+wrap(t3,ctx)
    }
    case TmAbs(x,t1) => {
      val (ctx2,x2) = ctx.pickFreshName(x)
      "(lambda %s. %s )".format(x2, print(t1,ctx2))
    }
    case TmApp(t1,t2) => "(%s %s)".format(print(t1,ctx),print(t2,ctx))
    case TmVar(x,n) => {
      if (ctx.length != n) throw new Exception("Bad index")
      ctx(x)
    }
    case TmLet(name,t1,t2) => "let %s=%s in %s".format(name, wrap(t1,ctx), wrap(t2,ctx))
  }
}
