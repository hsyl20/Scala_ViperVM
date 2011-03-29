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

  object CODE {

    trait VODDStart {
      var _typ: Tree = EmptyTree
      
      def withType(typ: Type): this.type = { 
        _typ = typ
        this
      }
    }

    class TreeMethods(target: Tree) {
      def DOT(member: String)       = SelectStart(Select(target, member))

      /** Assignment */
      def ===(rhs: Tree) = Assign(target, rhs)
    }

    def MODULE(name:String)(trees:Tree*) = new Module(name, trees.toList)
    def VAL(name:String): ValTreeStart = new ValTreeStart(name)
    def LIT(value:Any): Literal = Literal(Constant(value))
    def FUNCTION(args:ValDef*)(typ:Tree)(body:Tree)  = new Function(args.toList, body, typ)
    def TYP(name:String): Type = Type(name)
    def ID(name:String): Ident = Ident(name)

    case class SelectStart(tree: Select) {
      def apply(args: Tree*) = Apply(tree, args.toList)
    }

    class ValTreeStart(name:String) extends VODDStart {
      final def ===(rhs:Tree) = ValDef(name, _typ, rhs)
    }

    implicit def mkTreeMethods(tree:Tree) = new TreeMethods(tree)

    /** (foo DOT bar) might be simply a Select, but more likely it is to be immediately
     *  followed by an Apply.  We don't want to add an actual apply method to arbitrary
     *  trees, so SelectStart is created with an apply - and if apply is not the next
     *  thing called, the implicit from SelectStart -> Tree will provide the tree.
     */
    implicit def mkTreeFromSelectStart(ss: SelectStart): Select = ss.tree
    implicit def mkTreeMethodsFromSelectStart(ss: SelectStart): TreeMethods = mkTreeMethods(ss.tree)
    implicit def mkTreeFromValStart(ss: ValTreeStart): ValDef = (ss === EmptyTree)
    implicit def mkTreeMethodsFromValStart(ss: ValTreeStart): TreeMethods = mkTreeMethods(ss === EmptyTree)
  }
}
