package org.vipervm.graph

import org.neo4s.Node

abstract class Expr {
    val peer:Node
}

/** A free variable coming from an abstraction */
case class FreeVar(val peer:Node) extends Expr

case class AbsExpr(val peer:Node) extends Expr

object Expr {
  def  apply(node:Node) = node.properties("kind") match {
    case "abs" => AbsExpr(node)
    case "apply" => new Application(node)
    case _ => throw new Exception("Unknown node type")
  }
}
