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

package org.vipervm.library

import org.vipervm.runtime.{Function,Rule,FunctionPrototype}
import org.vipervm.runtime.mm.VVMType

class Library(functions:Map[FunctionPrototype,Seq[Function]],rules:Map[FunctionPrototype,Seq[Rule]]) {

  def proto(name:String,paramTypes:Seq[VVMType]):FunctionPrototype = {
    protoOption(name,paramTypes).getOrElse {
      throw new Exception("Prototype not found: %s(%s)".format(name,paramTypes.mkString(",")))
    }
  }

  def protoOption(name:String,paramTypes:Seq[VVMType]):Option[FunctionPrototype] = {
    val ps = functions.keys.filter(f => f.name == name && f.resultType(paramTypes).isDefined)
    ps.headOption
  }

  def functionsByProto(proto:FunctionPrototype):Seq[Function] = functions(proto)
  def rulesByProto(proto:FunctionPrototype):Seq[Rule] = rules(proto)
}

object Library {
  def apply(functions:Function*)(rules:Rule*) = {
    new Library(functions.groupBy(_.prototype),rules.groupBy(_.prototype))
  }
}
