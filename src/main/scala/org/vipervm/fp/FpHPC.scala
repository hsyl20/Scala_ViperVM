package org.vipervm.fp

sealed abstract class FpTerm

case class Data(id:Int) extends FpTerm
case class Cod(name:String, arity:Int) extends FpTerm
case class CodInstance(codelet:Cod, args:Seq[FpTerm]) extends FpTerm
case class App(t1:FpTerm,t2:FpTerm) extends FpTerm

object FpTerm {
  object NoRuleApplies extends Exception

  def eval(t:FpTerm):FpTerm = t match {
    case App(c@Cod(name,arity),d@Data(id)) => {
      val inst = CodInstance(c,Seq(d))
      if (arity == 1) {
        schedule(inst)
      }
      else inst
    }
    case App(c@CodInstance(cod,args),d@Data(id)) => {
      val inst = CodInstance(cod, args :+ d)
      if (cod.arity == inst.args.length) {
        schedule(inst)
      }
      else inst
    }
    case App(t,e) => eval(App(eval(t),eval(e)))
    case _ => t
  }

  var instances:Map[CodInstance,String] = Map.empty
  var cnt:Int = 0

  def schedule(ci:CodInstance):Data = {
    val d = Data(cnt)
    cnt += 1
    println("Schedule Data(%s) = %s(%s)".format(
      d, ci.codelet.name, ci.args.mkString(",")))
    d
  }

  def ??? = throw new Exception("Undefined")
}
