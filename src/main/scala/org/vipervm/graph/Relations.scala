package org.vipervm.graph

import org.neo4s.Relation

object Relations {

  val Defs = Relation("defs")
  val Def = Relation("def")

  val ApplyArg = Relation("applyArg")

  val storedIn = Relation("storedIn")   /** Indicate in which memory a data is stored */
  val freeVarOf = Relation("freeVarOf") /** Reference to the free variable of an abstraction */
  val absExpr  = Relation("absExpr")    /** Argument of an expression */

  val operators = Relation("operators") /** Links to predefined operators */
  val operator = Relation("operator")   /** A predefined operator */

  val what = Relation("what")           /** Main: what to compute */
  val whereTo = Relation("whereTo")     /** Main: where should it be actually stored  */
  val main = Relation("main")           /** Main node  */

  val typeKey = "type"
  val applicationType = "application"
  val abstractionType = "abstraction"

  val nameKey = "name"
}
