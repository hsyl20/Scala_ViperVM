package org.vipervm.graph

import Relations._
import org.neo4s.Direction._
import org.neo4s.DataBase

class Program(db:DataBase) {
  val defs = db.root.getSingleRelationship(Defs, Outgoing).end

  def addFunction(name:String): Fun = {
    db.transaction {

      val node = db.createNode
      node.properties += "name" -> name

      defs --Def--> node

      val idx = db.nodeIndex("functions")
      idx.add(node, Function.toString, name)

      new Fun(node)
    }.get
  }
}
