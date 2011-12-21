package org.vipervm.fp

object Printer {

  def needWrap(t:Term):Boolean = t match {
    case TmTrue(_) => false
    case TmFalse(_) => false
    case TmZero(_) => false
    case _ => true
  }

  def wrap(t:Term):String = {
    if (!needWrap(t)) print(t) else "("+print(t)+")"
  }

  def print(t:Term):String = t match {
    case TmTrue(_) => "true"
    case TmFalse(_) => "false"
    case TmIf(_,t1,t2,t3) => {
      "if "+wrap(t1)+" then "+wrap(t2)+" else "+wrap(t3)
    }
    case TmZero(_) => "0"
    case TmSucc(_,t1) => "succ "+wrap(t1)
    case TmPred(_,t1) => "pred "+wrap(t1)
    case TmIsZero(_,t1) => "isZero "+wrap(t1)
  }
}
