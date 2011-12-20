package org.vipervm.graph

import org.neo4s.Direction._
import org.neo4s.{DataBase,Node}
import Relations._

class Abstraction(val peer:Node) {
  /** Expression associated to this abstraction */
  def expr = peer.getSingleRelationship(absExpr, Outgoing)

  /** Free variable */
  def variable = peer.getSingleRelationship(freeVarOf, Incoming)
}

object Abstraction {
  /** Create a new abstraction node */
  def create(fun:Expr=>Expr)(implicit db:DataBase) = {
    val abs = db.createNode
    abs.properties += typeKey -> abstractionType
    val freeVar = new FreeVar(db.createNode)
    abs --absExpr--> fun(freeVar).peer
    freeVar.peer --freeVarOf--> abs
    new Application(abs)
  }
}
