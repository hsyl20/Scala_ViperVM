package org.vipervm.fp

import scala.util.parsing.combinator.syntactical._

object Parser extends StandardTokenParsers {

  lexical.reserved += ("true", "false", "if", "then", "else", "let", "in")

  lexical.delimiters += ("(", ")", "=")

  lazy val root: Parser[Term] = term

  lazy val term:Parser[Term] = (
      "true" ^^^ TmTrue
    | "false" ^^^ TmFalse
    | "if"~>term~("then"~>term)~("else"~>term) ^^ {
        case t1~t2~t3 => TmIf(t1,t2,t3)
      }
    | "("~>term<~")"
    | "let"~>ident~("="~>term)~("in"~>term) ^^ {
        case name~t1~t2 => TmLet(name,t1,t2)
      }
    | term~term ^^ {
        case t1~t2 => TmApp(t1,t2)
      }
  )

  def parse(s:String) = {
    val tokens = new lexical.Scanner(s)
    phrase(root)(tokens)
  } 
}
