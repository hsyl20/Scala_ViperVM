package org.neo4s

import org.neo4j.graphdb.PropertyContainer

import scala.collection.JavaConversions._

class PropertyContainerWrapper(peer:PropertyContainer) extends scala.collection.mutable.Map[String,AnyRef] {

  def get(key:String):Option[AnyRef] = Option(peer.getProperty(key,null))

  def iterator = {
    peer.getPropertyKeys.iterator.map(x => (x,peer.getProperty(x)))
  }

  def -=(key:String) = {
    peer.removeProperty(key)
    this
  }

  def +=(kv:(String,AnyRef)) = {
    peer.setProperty(kv._1,kv._2)
    this
  }
}
