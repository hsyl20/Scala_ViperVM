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

package org.vipervm.dsl.functional

sealed abstract class Term
/* Booleans */
case object TmTrue extends Term
case object TmFalse extends Term
/* Conditionals */
case class TmIf(cond:Term,then:Term,els:Term) extends Term
/* Naturals */
case object TmZero extends Term
case class TmSucc(n:Term) extends Term
case class TmPred(n:Term) extends Term
case class TmIsZero(n:Term) extends Term


case class NoRuleApplies(term:Term) extends Exception

object Term {
  /**
   * Indicate if a term is a numeric value
   */
  def isNumericVal(t:Term):Boolean = t match {
    case TmZero => true
    case TmSucc(t1) => isNumericVal(t1)
    case _ => false
  }

  /**
   * Indicate if a term is a value
   */
  def isVal(t:Term):Boolean = t match {
    case TmTrue => true
    case TmFalse => true
    case t if isNumericVal(t) => true
    case _ => false
  }

  /**
   * Single-step evaluator
   */
  def eval1(t:Term):Term = t match {
    case TmIf(TmTrue,t2,_) => t2
    case TmIf(TmFalse,_,t3) => t3
    case TmIf(t1,t2,t3) => TmIf(eval1(t1),t2,t3)
    case TmSucc(t1) => TmSucc(eval1(t1))
    case TmPred(TmZero) => TmZero
    case TmPred(TmSucc(nv1)) if isNumericVal(nv1) => nv1
    case TmPred(t1) => TmPred(eval1(t1))
    case TmIsZero(TmZero) => TmTrue
    case TmIsZero(TmSucc(nv1)) if isNumericVal(nv1) => TmFalse
    case TmIsZero(t1) => TmIsZero(eval1(t1))
    case _ => throw new NoRuleApplies(t)
  }

  def eval(t:Term):Term = try eval(eval1(t)) catch {
    case NoRuleApplies(t) => t
  }
}
