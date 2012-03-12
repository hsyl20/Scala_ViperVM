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

package org.vipervm.parsers

import scala.util.parsing.combinator.RegexParsers

import org.vipervm.runtime._
import org.vipervm.runtime.interpreter._

object LispyParser extends RegexParsers {

  val expr: Parser[Term] = (
/*      ("("~>"let"~>"("~>rep("("~>id~expr<~")")<~")")~expr<~")" ^^ {
        case lets~in => (lets :\ in) { case ((v~e),in) =>
          TmLet(v,e,in)
        }
      }
    |*/ ("("~>id)~rep(expr)<~")" ^^ {
        case k~as => TmApp(k, Vector(as:_*))
      }
    | id
  )

  def id : Parser[TmId] = """[a-zA-Z=*+/<>!\?][a-zA-Z0-9=*+/<>!\?]*""".r ^^ {case s => TmId(s)}

  def parse(s:String):ParseResult[Term] = parse(expr,s)
}
