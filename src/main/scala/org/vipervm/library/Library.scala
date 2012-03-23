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

import org.vipervm.runtime.{MetaKernel,Rule,FunctionPrototype}
import org.vipervm.runtime.mm.VVMType

class Library(functions:Map[FunctionPrototype,Seq[MetaKernel]],rules:Map[FunctionPrototype,Seq[Rule]]) {

  def proto(name:String,paramTypes:Seq[VVMType]):FunctionPrototype = {
    protoOption(name,paramTypes).getOrElse {
      throw new Exception("Prototype not found: %s(%s)".format(name,paramTypes.mkString(",")))
    }
  }

  def protoOption(name:String,paramTypes:Seq[VVMType]):Option[FunctionPrototype] = {
    val ps = functions.keys.filter(f => f.name == name && f.resultType(paramTypes).isDefined)
    ps.headOption
  }

  def functionsByProto(proto:FunctionPrototype):Seq[MetaKernel] = functions.getOrElse(proto, Seq.empty)
  def rulesByProto(proto:FunctionPrototype):Seq[Rule] = rules.getOrElse(proto, Seq.empty)
}

object Library {
  def apply(functions:MetaKernel*)(rules:Rule*) = {
    new Library(functions.groupBy(_.prototype),rules.groupBy(_.prototype))
  }
}
