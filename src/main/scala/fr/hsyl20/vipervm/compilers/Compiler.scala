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

package fr.hsyl20.vipervm.compilers

import fr.hsyl20.vipervm.compilers.ast._

class Compiler {
  private val out = new StringBuilder
  private val symtab = new HierarchicalSymbolTable

  def compile(t:Tree):String = {
    
    /*t match {
      case ValDef(name, typ, rhs) => {
        symtab.put(name, Def(rhs))
      }
      case Package(name, members) => members map compile
      case Select(qualifier, name) =>
    }*/
    ""
  }
}
