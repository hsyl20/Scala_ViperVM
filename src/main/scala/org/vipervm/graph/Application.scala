package org.vipervm.graph

import org.neo4s.Direction._
import org.neo4s.{DataBase,Node}
import Relations._

class Application(val peer:Node) extends Expr {
  /** Application parameters */
  val args:Seq[Expr] = peer.relationships.map(r =>
    (r.properties("number").asInstanceOf[Int],Expr(r.end))
  ).toSeq.sortBy(_._1).map(_._2)
}

object Application {

  /** Create a new application node */
  def create(args:Expr*)(implicit db:DataBase):Application = {

    val app = db.createNode
    app.properties += typeKey -> applicationType

    for ((a,i) <- args.zipWithIndex) {
      val rel = app --ApplyArg--> a.peer
      rel.properties += "number" -> (i:java.lang.Integer)
    }

    new Application(app)
  }

}
