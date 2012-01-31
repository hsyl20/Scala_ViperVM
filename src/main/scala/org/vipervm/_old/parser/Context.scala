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

package org.vipervm.parser

case class InvalidPathException(valid:List[String],invalid:List[String]) extends Exception("Invalid path: "+valid.mkString("",".",".")+" !! "+invalid.mkString("."))

sealed abstract class Entity

class Context extends Entity {

  val contents: Map[String,Entity] = Map.empty
  
  def get(path:String):Entity = get(path.split('.').toList)

  def get(path:List[String]):Entity = innerGet(path)

  protected def innerGet(path:List[String],valid:List[String]=Nil): Entity = {
    path match {
      case a::as => contents.get(a) match {
        case None => throw InvalidPathException(valid.reverse,path)
        case Some(e) => {
          as match {
            case Nil => e
            case _ => e match {
              case e:Context => e.innerGet(as, a::valid)
              case _ => throw new Exception("Unsupported operation")
            }
          }
        }
      }
      case Nil => throw InvalidPathException(valid.reverse, path)
    }
  }

  def +(kv:(String,Entity)):Context = {
    val cs = contents + (kv._1 -> kv._2)
    new Context {
      override val contents = cs
    }
  }
}

object Context {
  def empty = new Context
}
