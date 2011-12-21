package org.vipervm.fp

import scala.annotation.tailrec

class Info
case object DummyInfo extends Info

sealed abstract class Term 

case class TmTrue(info:Info) extends Term
case class TmFalse(info:Info) extends Term
case class TmIf(info:Info,cond:Term,left:Term,right:Term) extends Term
case class TmZero(info:Info) extends Term
case class TmSucc(info:Info,term:Term) extends Term
case class TmPred(info:Info,term:Term) extends Term
case class TmIsZero(info:Info,term:Term) extends Term

object Term {

  /** Indicate whether a term is a numeric value */
  def isNumericValue(t:Term):Boolean = t match {
    case TmZero(_) => true
    case TmSucc(_,t1) => isNumericValue(t1)
    case _ => false
  }

  /** Indicate whether a term is a value */
  def isValue(t:Term):Boolean = t match {
    case TmTrue(_) => true
    case TmFalse(_) => true
    case e if isNumericValue(e) => true
    case _ => false
  }

  case object NoRuleApplies extends Exception

  /**
   * Single step evaluation
   */
  def eval(t:Term):Term = try {
    eval(eval1(t))
  }
  catch {
    case NoRuleApplies => t
  }

  def eval1(t:Term):Term = t match {
    case TmIf(_,TmTrue(_),t2,_)   => t2
    case TmIf(_,TmFalse(_),_,t3)  => t3
    case TmIf(fi,t1,t2,t3)        => TmIf(fi,eval1(t1),t2,t3)
    case TmSucc(fi,t1)            => TmSucc(fi,eval1(t1))
    case TmPred(_,TmZero(_))      => TmZero(DummyInfo)
    case TmPred(_,TmSucc(_,nv1)) if isNumericValue(nv1) => nv1
    case TmPred(fi,t1)            => TmPred(fi,eval1(t1))
    case TmIsZero(_,TmZero(_))    => TmTrue(DummyInfo)
    case TmIsZero(_,TmSucc(_,nv1)) if isNumericValue(nv1) => TmFalse(DummyInfo)
    case TmIsZero(fi,t1)          => TmIsZero(fi,eval1(t1))
    case _                        => throw NoRuleApplies
  }
}
