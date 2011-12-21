package org.vipervm.fp

import scala.util.parsing.combinator.syntactical._

object Parser extends StandardTokenParsers {

  lexical.reserved += ("true", "false", "if", "then", "else", "pred", "succ", "isZero")

  lexical.delimiters += ("(", ")")

  lazy val root: Parser[Term] = term

  lazy val term:Parser[Term] = (
      "true" ^^^ TmTrue(DummyInfo)
    | "false" ^^^ TmFalse(DummyInfo)
    | "if"~>term~("then"~>term)~("else"~>term) ^^ {
        case t1~t2~t3 => TmIf(DummyInfo,t1,t2,t3)
      }
    | numericLit ^^^ TmZero(DummyInfo)
    | "succ"~>term ^^ {
        case t => TmSucc(DummyInfo,t)
      }
    | "pred"~>term ^^ {
        case t => TmPred(DummyInfo,t)
      }
    | "isZero"~>term ^^ {
        case t => TmIsZero(DummyInfo,t)
      }
    | "("~>term<~")"
  )

  def parse(s:String) = {
    val tokens = new lexical.Scanner(s)
    phrase(root)(tokens)
  } 
}
