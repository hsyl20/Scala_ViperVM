package org.vipervm.graph

import org.neo4s.Neo4s._
import org.neo4s.DataBase
import Relations._

object Graph {
  val predefinedOperators = Seq("map", "reduce", "zipWith", "crossWith")

  def initialize(implicit db:DataBase) = {
    /* Add operators predefined operators */
    val ops = db.createNode
    db.root --operators--> ops

    for (op <- predefinedOperators) {
      val n = db.createNode
      n.properties += nameKey -> op
      ops --operator--> n
    }

    /* Add main entry */
    val main = db.createNode
    db.root --Relations.main--> main
  }
}
