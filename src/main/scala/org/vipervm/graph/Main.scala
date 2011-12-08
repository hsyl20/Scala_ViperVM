package org.vipervm.graph

import org.neo4s.Node
import org.neo4s.Direction._

class Main(val peer:Node) {
  def what = peer.getSingleRelationship(Relations.what, Outgoing)
  def whereTo = peer.getSingleRelationship(Relations.whereTo, Outgoing)
}
