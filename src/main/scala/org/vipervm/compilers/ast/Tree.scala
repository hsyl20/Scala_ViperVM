/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.compilers.ast

sealed abstract class Tree {
  def isEmpty: Boolean = this match {
    case EmptyTree => true
    case _ => false
  }
}

case object EmptyTree extends Tree

case class ValDef(name: String, typ:Tree, rhs:Tree) extends Tree
case class Function(params:List[ValDef], body:Tree, returnTyp:Tree) extends Tree
case class If(cond:Tree, thenp:Tree, elsep:Tree) extends Tree
case class Ident(name:String) extends Tree
case class Literal(value:Constant) extends Tree
case class Package(name:String, members:List[Tree]) extends Tree
case class Bind(name:String, body:Tree) extends Tree
case class Assign(lhs:Tree,rhs:Tree) extends Tree
case class Apply(fun: Tree, args: List[Tree]) extends Tree
case class Select(qualifier: Tree, name: String) extends Tree

trait TypTree extends Tree
case class Type(name:String) extends TypTree
