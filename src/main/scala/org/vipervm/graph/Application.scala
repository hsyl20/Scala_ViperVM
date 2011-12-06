package org.vipervm.graph

import org.neo4j.graphdb.Node

import org.neo4s.Neo4s._
import org.neo4s.Direction._
import org.neo4s.DataBase
import Relations._

class Application(val peer:Node) {
  /** Function to which is applied something */
  def fun = peer.getSingleRelationship(applyFun, Outgoing)

  /** Argument of the application */
  def arg = peer.getSingleRelationship(applyArg, Outgoing)
}

object Application {
  /** Create a new application node */
  def create(fun:Expr, arg:Expr)(implicit db:DataBase) = {
    val app = db.createNode
    app.properties += typeKey -> applicationType
    app--applyFun-->fun.peer
    app--applyArg-->arg.peer
    new Application(app)
  }
}
