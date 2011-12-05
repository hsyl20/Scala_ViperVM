package org.neo4s

import org.neo4j.graphdb.{Node,PropertyContainer}

object Neo4s {

  /**
   * Wrapper for Neo4j entities
   */
  implicit def nodeWrap(n:Node) = new NodeWrapper(n)
  implicit def propertyContainerWrap(p:PropertyContainer) = new PropertyContainerWrapper(p)

}
