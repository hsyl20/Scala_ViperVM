package org.vipervm.graph


import org.neo4s.Node

abstract class Expr {
    val peer:Node
}

/** A free variable coming from an abstraction */
case class FreeVar(val peer:Node) extends Expr

/** A predefined operator */
case class Operator(val peer:Node) extends Expr
