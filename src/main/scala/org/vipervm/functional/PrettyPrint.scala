package org.vipervm.functional

import scala.annotation.tailrec

object PrettyPrint {
  private val alpha = ('a' to 'z').map(_.toString)

  def letters(i:Int,s:String = ""): String = {
    val x = i % alpha.length
    val ss = s + alpha(x)
    val y = i / alpha.length
    if (y == 0) ss else letters(y,ss)
  }

  @tailrec
  def newName(p:Program,nvar:Int):(String,Int) = {
    val name = letters(nvar)
    if (p.exprs.contains(name)) newName(p,nvar+1) else (name,nvar+1)
  }

  def stringify(p:Program,e:Expr,nvar:Int,top:Boolean=false): String = e match {
    case Abstraction(f) => {
      val (name,nv) = newName(p,nvar)
      val e = Var(name)
      "\\%s -> %s".format(name,stringify(p,f(e),nv,true))
    }
    case Application(e,arg) => {
      if (top) {
        val lhs = stringify(p,e,nvar,top)
        val rhs = stringify(p,arg,nvar,false)
        "%s %s".format(lhs,rhs)
      } else {
        val lhs = stringify(p,e,nvar,true)
        val rhs = stringify(p,arg,nvar,false)
        "(%s %s)".format(lhs,rhs)
      }
    }
    case Var(s) => s
    case FMap => "map"
    case Reduce => "reduce"
    case ZipWith => "zipWith"
    case CrossWith => "crossWith"
    case Transpose => "transpose"
    case Split  => "split"
    case Join => "join"
    case Plus => "(+)"
    case Minus => "(-)"
    case Time => "(*)"
    case _ => sys.error("Unknown expression %s".format(e))
  }

  def perform(program:Program):String = {
    val sb = new StringBuilder
    program.exprs.foreach { case (k,v) =>
      sb append k
      sb append " = "
      sb append stringify(program,v,0,true)
      sb append "\n"
    }
    sb.toString
  }

}
