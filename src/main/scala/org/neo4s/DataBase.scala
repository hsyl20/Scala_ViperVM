package org.neo4s

import org.neo4j.kernel.{EmbeddedGraphDatabase => EGDB}
import org.neo4j.graphdb.{Node,PropertyContainer}

import scala.collection.JavaConversions._
import org.neo4s.Neo4s._

class DataBase(path:String) {

  protected val peer = new EGDB(path)

  /* Shutdown the database when the JVM exits */
  Runtime.getRuntime.addShutdownHook( new Thread {
    override def run:Unit = peer.shutdown
  })

  /**
   * Perform the given closure in a transaction
   */
  def transaction(body: => Unit):Boolean = {
    val tx = peer.beginTx
    try {
      body
      tx.success
      tx.finish
      true
    }
    catch {
      case _ => {
        tx.failure
        tx.finish
        false
      }
    }
  }

  /**
   * Create a new node
   */
  def createNode:Node = peer.createNode

  /**
   * Return all nodes of the graph
   */
  def allNodes:Iterable[Node] = peer.getAllNodes

  /**
   * Return the root node of the graph
   */
  def root = peer.getReferenceNode

  /**
   * Clear a database
   */
  def clear():Unit = allNodes foreach { n => 
    n.relationships foreach (_.delete)
    n.delete
  }

}

