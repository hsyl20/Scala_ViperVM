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
import org.vipervm.runtime.FunctionalKernel

sealed abstract class Term {
  def apply(ts:Term*) = (this /: ts.toList)(AppTerm(_,_))

  private def map(f:(VarTerm,Int)=>Term,c:Int):Term = this match {
    case t@VarTerm(_,_) => f(t,c)
    case AbsTerm(body,name) => AbsTerm(body.map(f,c+1),name)
    case AppTerm(left,right) => AppTerm(left.map(f,c),right.map(f,c))
  }

  def shift(d:Int):Term = {
    val f = (v:VarTerm,c:Int) => {
      if (v.idx >= c) VarTerm(v.idx+d,v.ctxSize+d) else VarTerm(v.idx, v.ctxSize+d)
    }
    this.map(f,0)
  }

  def subst(j:Int,s:Term):Term = {
    val f = (v:VarTerm,c:Int) => {
      if (v.idx == j+c) s.shift(c) else VarTerm(v.idx,v.ctxSize)
    }
    this.map(f,0)
  }

  def substTop(s:Term):Term = {
    subst(0,s.shift(1)).shift(-1)
  }
}

/**
 * A variable with its de Bruijn index
 * ctxSize is used to check that context size is coherent
 */
case class VarTerm(idx:Int,ctxSize:Int) extends Term

/**
 * An abstraction with its content
 * name field can be used to store the variable name
 */
case class AbsTerm(body:Term, name:String = "") extends Term

/** An application */
case class AppTerm(left:Term, right:Term) extends Term

case class DataTerm(data:Data) extends Term
case class KernelTerm(kernel:FunctionalKernel) extends Term
