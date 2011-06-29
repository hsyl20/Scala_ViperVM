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

import scala.util.parsing.combinator.syntactical._

case class MethodCall(self:String, name:String, args:List[Any])
case class FunDef(name:String, params:List[String], body:Any)
case class ValDef(name:String, body:Any)
case class Closure(params:List[String],body:Any)
case class Module(qname:List[String], body:Any)
case class Import(qname:List[String])

object Parser extends StandardTokenParsers {

  lexical.reserved += ("def", "val", "package", "import")

  lexical.delimiters += ("{", "}", "(", ")", ",", "=", ".", ";", "=>")

  lazy val unit: Parser[Any] = rep (
      module
    | imports<~opt(";")
    | fundef
  )

  lazy val fundef: Parser[Any] = "def"~>(ident<~"(")~(repsep(ident,",")<~")"<~"=")~body ^^ {
    case name~params~body => FunDef(name,params,body)
  }

  lazy val body: Parser[Any] = (
      block
    | imports
    | valdef
    | closure
    | (ident<~".")~ident~opt("("~>repsep(body,",")<~")") ^^ {
        case self~name~Some(args) => MethodCall(self,name,args)
        case self~name~None       => MethodCall(self,name,Nil)
      }
    | ident
  )

  lazy val block: Parser[Any] = "{"~>repsep(body,";")<~opt(";")<~"}"

  lazy val valdef: Parser[Any] = "val"~>(ident<~"=")~body ^^ {
    case name~body => ValDef(name,body)
  }

  lazy val closure: Parser[Any] = (
    (("("~>repsep(ident,",")<~")") | ident ^^ {case a => List(a)} )~("=>"~>body) ^^ {
      case params~body => Closure(params,body)
    }
  )

  lazy val module: Parser[Any] = "package"~>repsep(ident,".")~("{"~>unit<~"}") ^^ {
    case qname~body => Module(qname,body)
  }

  lazy val imports: Parser[Any] = "import"~>repsep(ident, ".") ^^ {
    case qname => Import(qname)
  }

  def parse(s:String) = {
    val tokens = new lexical.Scanner(s)
    phrase(unit)(tokens)
  } 
}
