package org.neo4s

import org.neo4j.graphdb.RelationshipType

case class Relation(val name:String) extends RelationshipType
