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

sealed abstract class Token
case class QName(path:String*) extends Token
case class MethodCall(self:String, name:String, args:List[Token]) extends Token
case class FunDef(name:String, params:List[String], body:Token) extends Token
case class ValDef(name:String, body:Token) extends Token
case class Closure(params:List[String],body:Token) extends Token
case class Module(qname:QName, body:Token*) extends Token
case class Import(qname:QName) extends Token
case class Block(statements:Token*) extends Token
case class Ident(name:String) extends Token


object Parser extends StandardTokenParsers {

  lexical.reserved += ("def", "val", "package", "import")

  lexical.delimiters += ("{", "}", "(", ")", ",", "=", ".", ";", "=>")

  lazy val root: Parser[Module] = unit ^^ {
    case ss => Module(QName("__root__"), ss:_*)
  }

  lazy val unit: Parser[List[Token]] = rep (
      module
    | imports<~opt(";")
    | fundef
  ) 

  lazy val fundef: Parser[FunDef] = "def"~>(ident<~"(")~(repsep(ident,",")<~")"<~"=")~body ^^ {
    case name~params~body => FunDef(name,params,body)
  }

  lazy val body: Parser[Token] = (
      block
    | imports
    | valdef
    | closure
    | (ident<~".")~ident~opt("("~>repsep(body,",")<~")") ^^ {
        case self~name~Some(args) => MethodCall(self,name,args)
        case self~name~None       => MethodCall(self,name,Nil)
      }
    | app
    | ident ^^ { case n => Ident(n) }
  )

  lazy val block: Parser[Block] = "{"~>repsep(body,";")<~opt(";")<~"}" ^^ {
    case ss => Block(ss:_*)
  }

  lazy val valdef: Parser[ValDef] = "val"~>(ident<~"=")~body ^^ {
    case name~body => ValDef(name,body)
  }

  lazy val closure: Parser[Closure] = (
    (("("~>repsep(ident,",")<~")") | ident ^^ {case a => List(a)} )~("=>"~>body) ^^ {
      case params~body => Closure(params,body)
    }
  )

  lazy val module: Parser[Module] = "package"~>repsep(ident,".")~("{"~>unit<~"}") ^^ {
    case qname~body => Module(QName(qname:_*),body:_*)
  }

  lazy val imports: Parser[Import] = "import"~>repsep(ident, ".") ^^ {
    case qname => Import(QName(qname:_*))
  }

  lazy val app: Parser[MethodCall] = ident~ident~body ^^ {
    case self~name~body => MethodCall(self,name,List(body))
  }

  def parse(s:String) = {
    val tokens = new lexical.Scanner(s)
    phrase(root)(tokens)
  } 
}
