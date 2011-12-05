package org.neo4s

import org.neo4j.graphdb.{Node,Relationship}
import scala.collection.JavaConversions._

class NodeWrapper(peer:Node){

  /* Funky syntax for node relationships */
  def -- (r:Relation) = new {
    def -->(n:Node) = peer.createRelationshipTo(n,r)
  }

  def properties = new PropertyContainerWrapper(peer)

  def relationships:Iterable[Relationship] = peer.getRelationships
}


