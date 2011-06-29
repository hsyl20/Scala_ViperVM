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

package org.vipervm.ir

/* Intermediate representation */
sealed abstract class Term

/* Booleans */
case object True extends Term
case object False extends Term

/* Conditionals */
case class If(cond:Term,then:Term,els:Term) extends Term

/* Application */
case class Apply(fn:Term, arg:Term) extends Term


case class NoRuleApplies(term:Term) extends Exception

object Term {
  def eval1(t:Term):Term = t match {
    case If(True,t2,_) => t2
    case If(False,_,t3) => t3
    case If(t1,t2,t3) => If(eval1(t1),t2,t3)
    case _ => throw new NoRuleApplies(t)
  }

  def eval(t:Term):Term = try eval(eval1(t)) catch {
    case NoRuleApplies(t) => t
  }
}
