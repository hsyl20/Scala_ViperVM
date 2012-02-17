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

import scala.util.parsing.combinator.syntactical._

import org.vipervm.runtime._
import org.vipervm.runtime.interpreter._

object LispyParser extends StandardTokenParsers {

  lexical.delimiters += ("(", ")")

  lazy val expr: Parser[Term] = (
      ("("~>ident)~rep(expr)<~")" ^^ {
        case k~as => TmApp(TmKernel(k), Vector(as:_*))
      }
    | ident ^^ {
        case d => TmVar(d)
      }
  )

  def parse(s:String) = {
    val tokens = new lexical.Scanner(s)
    phrase(expr)(tokens)
  } 
}
