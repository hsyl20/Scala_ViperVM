package org.neo4s

import org.neo4j.graphdb.{Node=>Nod,Relationship,RelationshipType,Direction=>Dir}
import scala.collection.JavaConversions._

class Node(val peer:Nod){

  /* Funky syntax for node relationships */
  def -- (r:Relation) = new {
    def -->(n:Node) = createRelationshipTo(n,r)
  }

  def createRelationshipTo(n:Node,r:Relation) = peer.createRelationshipTo(n.peer,r)

  def properties = new PropertyContainerWrapper(peer)

  def relationships:Iterable[Relationship] = peer.getRelationships

  def delete() = peer.delete

  def getSingleRelationship(typ:RelationshipType,direction:Dir) = Option(peer.getSingleRelationship(typ,direction))
}


