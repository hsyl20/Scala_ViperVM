/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.compilers.ast

import java.io.{PrintWriter,Writer}

class TreePrinter(out: PrintWriter) {
  protected var indentMargin = 0
  protected val indentStep = 2
  protected var indentString = "                                        " // 40

  def flush() = out.flush()

  def indent = indentMargin += indentStep
  def undent = indentMargin -= indentStep

  def println() {
    out.println()
    while (indentMargin > indentString.length())
      indentString += indentString
    if (indentMargin > 0)
      out.write(indentString, 0, indentMargin)
  }
  
  def backquotedPath(t: Tree): String = t match {
    case Select(qual, name) => "%s.%s".format(backquotedPath(qual), name)
    case Ident(name)        => name
    case _                  => t.toString
  }

  def printSeq[a](ls: List[a])(printelem: a => Unit)(printsep: => Unit) {
    ls match {
      case List() =>
      case List(x) => printelem(x)
      case x :: rest => printelem(x); printsep; printSeq(rest)(printelem)(printsep)
    }
  }

  def printColumn(ts: List[Tree], start: String, sep: String, end: String) {
    print(start); indent; println()
    printSeq(ts){print}{print(sep); println()}; undent; println(); print(end)
  }

  def printRow(ts: List[Tree], start: String, sep: String, end: String) {
    print(start); printSeq(ts){print}{print(sep)}; print(end)
  }

  def printRow(ts: List[Tree], sep: String) { printRow(ts, "", sep, "") }

  def printOpt(prefix: String, tree: Tree) {
    if (!tree.isEmpty) { print(prefix); print(tree) }
  }

  def printValueParams(ts: List[ValDef]) {
    print("(")
    printSeq(ts){printParam}{print(", ")}
    print(")")
  }

  def printParam(tree: Tree) {
    tree match {
      case ValDef(name, tp, rhs) =>
        print(name); printOpt(": ", tp); printOpt(" = ", rhs)
    }
  }

  def print(str: String) { out.print(str) }

  def printRaw(tree:Tree) {
    tree match {
      case EmptyTree =>
        print("<Empty>")
      case ValDef(name,typ,rhs) => {
        print("val ")
        print(name)
        printOpt(": ", typ)
        print(" = ")
        print(rhs)
      }
      case Package(name, members) => {
        print("package %s ".format(name))
        printColumn(members, "{", "", "}")
      }
      case Literal(x) =>
        print(x.escapedStringValue)
      case Function(params, body, returnTyp) => {
        print("("); printValueParams(params); print(" => "); print(body); print(")")
      }
      case Type(name) => print(name)
      case Apply(fun, vargs) => print(fun); printRow(vargs, "(", ", ", ")")
      case Select(qualifier, name) =>
        print(backquotedPath(qualifier)); print("."); print(name)
      case Ident(name) => print(name)
    }
  }

  def print(tree:Tree) {
    printRaw(tree)
  }
}

object TreePrinter {

  object ConsoleWriter extends Writer {
    override def write(str: String) { Console.print(str) }
    
    def write(cbuf: Array[Char], off: Int, len: Int) {
      write(new String(cbuf, off, len))
    }
    
    def close = { /* do nothing */ }
    def flush = { /* do nothing */ }
  }
}
