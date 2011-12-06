package org.vipervm.graph

import org.neo4j.graphdb.Node
import org.neo4s.Direction._

class Main(val peer:Node) {
  def what = Option(peer.getSingleRelationship(Relations.what, Outgoing))
  def whereTo = Option(peer.getSingleRelationship(Relations.whereTo, Outgoing))
}
