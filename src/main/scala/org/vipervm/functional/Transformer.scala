package org.vipervm.functional

object Transformer {
  def replace(program:Program,from:Expr,to:Expr):Program = {
    new Program(program.exprs.map(x => x._1 -> ereplace(x._2,from,to)))
  }

  def ereplace(e:Expr,from:Expr,to:Expr):Expr = if (e == from) to else e match {
    case Abstraction(f) => Abstraction(x => ereplace(f(x),from,to))
    case Application(e,arg) => Application(ereplace(e,from,to),ereplace(arg,from,to))
    case e => e
  }
}
