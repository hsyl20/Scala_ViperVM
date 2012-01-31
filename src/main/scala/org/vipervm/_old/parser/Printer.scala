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

import java.io.StringWriter
import scala.text._

object Printer {

  val indent = 2

  private def mapreduce(ds:Seq[Token], sep:String = ""):Document = {
    if (ds.isEmpty) DocNil else {
      if (sep == "")
        ds.map(doc _).reduce(_ :/: _)
      else
        ds.map(doc _).reduce(_ :: DocText(sep) :/: _)
    }
  }

  def doc(t:Token): Document = t match {
    case QName(path@_*) => DocText(path.mkString("."))
    case MethodCall(self,name,args) => (
         DocText(self+"."+name+"(")
      :: mapreduce(args, ",")
      :: DocText(")")
    )
    case FunDef(name,params,body) => DocText("def "+name+params.mkString("(",",",")") + " = ") :: doc(body)
    case ValDef(name,body) => DocText("val "+name+" = ") :: doc(body)
    case Closure(params,body) => DocText(params.mkString("(",",",")") + "=>") :: doc(body)
    case Module(qname,body@_*) => (
         DocText("package ")
      :: doc(qname)
      :: DocText("{")
      :/: DocNest(indent, mapreduce(body))
      :/: DocText("}")
    )
    case Import(qname) => DocText("import ") :: doc(qname)
    case Block(statements@_*) => (
         DocText("{")
      :/: DocNest(indent, mapreduce(statements, ";"))
      :/: DocText("}")
    )
    case Ident(name) => DocText(name)
  }

  def print(t:Token): String = {
    val sw = new StringWriter
    doc(t).format(80, sw)
    sw.toString
  }
}
