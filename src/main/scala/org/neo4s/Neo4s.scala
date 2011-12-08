package org.neo4s

import org.neo4j.graphdb.{Node=>Nod,PropertyContainer}

object Neo4s {

  /**
   * Wrapper for Neo4j entities
   */
  implicit def nodeWrap(n:Nod) = new Node(n)
  implicit def propertyContainerWrap(p:PropertyContainer) = new PropertyContainerWrapper(p)

}
