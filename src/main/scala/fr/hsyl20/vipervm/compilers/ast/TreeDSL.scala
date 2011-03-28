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

package fr.hsyl20.vipervm.compilers.ast

trait TreeDSL {

  class TreeMethods(target: Tree) {
    /** Assignment */
    def ===(rhs: Tree) = Assign(target, rhs)
  }

  object CODE {
    def MODULE(name:String)(trees:Tree*) = new Module(name, trees.toList)
    def VAL(name:String): ValTreeStart = new ValTreeStart(name)
    def LIT(value:Any): Literal = Literal(Constant(value))

    implicit def treeToTreeMethods(tree:Tree) = new TreeMethods(tree)
  }

  class ValTreeStart(name:String) {
    final def ===(rhs:Tree) = ValDef(name, EmptyTree, rhs)
  }
}
