package org.neo4s

import org.neo4j.graphdb.{Direction => Dir}

object Direction {
  val Both = Dir.BOTH
  val Outgoing = Dir.OUTGOING
  val Incoming = Dir.INCOMING
}
