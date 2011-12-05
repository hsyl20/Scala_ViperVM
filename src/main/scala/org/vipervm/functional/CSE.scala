package org.vipervm.functional

/**
 * Common sub-expressions elimination
 */
object CSE {
  def perform(program:Program):Program = {
    //TODO
    /* val r0 = flatten(program) */
    val r1 = reverse(program)
    new Program(r1.map(_.swap))
  }

  def reverse(program:Program,ctx:Map[Expr,String] = Map.empty):Map[Expr,String] = {
    if (program.exprs.isEmpty) {
      ctx
    }
    else {
      val (name,e) = program.exprs.head
      val ee = ctx.get(e)
      val pr = new Program(program.exprs.tail)
      ee match {
        case None => {
          reverse(pr,ctx + (e -> name))
        }
        case Some(name2) => {
          val pr2 = Transformer.replace(pr,Var(name),Var(name2))
          val ctx2 = ctx.map(x => Transformer.ereplace(x._1,Var(name),Var(name2)) -> x._2)
          reverse(pr2,ctx2)
        }
      }
    }
  }

  /*def flatten(ms:Map[Expr,String],ctx:Map[Expr,String] = Map.empty):Map[Expr,String] = {
    if (ms.isEmpty) ctx else flatten(ms.tail, eflatten(ms.head,ctx))
  }

  def eflatten(e:Expr,ctx:Map[Expr,String]):Map[Expr,String] = e match {
    case Abstraction(f) => {
      val ctx2 = eflatten(
    }
  }*/

  /*def isNameInvalid(program:Program,name:String):Boolean = 
    program.exprs.contains(name)
  }*/
}
