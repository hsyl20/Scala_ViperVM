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

import fr.hsyl20.vipervm.compilers.ast.Tree
import scala.collection.mutable.HashMap

sealed abstract class SymbolValue
case class Package(symtab:SymbolTable) extends SymbolValue
case class Def(d:Tree) extends SymbolValue

class SymbolTable extends HashMap[String,SymbolValue] {

  def declare(item: (String,SymbolValue)) = {
    this += (item._1 -> item._2)
    this
  }
}

object SymbolTable {
   def empty: SymbolTable = new SymbolTable
}


/**
 * A hierarchical symbol table
 * 
 * It takes into account packages, imports and scopes
 */
class HierarchicalSymbolTable {

  private val root: SymbolTable = SymbolTable.empty
  private var current: List[SymbolTable] = List(root)
  
  private def getFrom(path:List[String], symtab:SymbolTable): Option[SymbolValue] = {
    if (path isEmpty) None
    else {
      symtab.get(path.head) match {
        case None => None
        case Some(Package(st)) => getFrom(path.drop(1), st)
        case a => a
      }
    }
  }

  private def getFromCurrent(path:List[String], curr:List[SymbolTable] = current): Option[SymbolValue] = {
    if (path isEmpty) None
    else {
      curr match {
        case Nil => None
        case a :: as => getFrom(path, a) match {
          case None => getFromCurrent(path, curr.drop(1))
          case a => a
        }
      }
    }
  }

  /** Return value associated to symbol s if found */
  def get(s:String): Option[SymbolValue] = {
    val path = s.split(".").toList

    path match {
      case a::as if a == "_root_" => getFrom(path.drop(1), root)
      case _   => getFromCurrent(path)
    }
  }

  def put(path:String, value:SymbolValue) {
    current.head += (path -> value)
  }


  def enterScope {
    current = SymbolTable.empty :: current
  }

  def exitScope {
    current = current.tail
  }
}
