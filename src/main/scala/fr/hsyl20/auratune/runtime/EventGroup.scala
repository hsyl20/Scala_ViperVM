/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**                     GPLv3
*/

package fr.hsyl20.auratune.runtime

import scala.concurrent.Lock

/**
 * An event that completes when all events it contains have completed
 */
class EventGroup[E <: Event](val events:Seq[E]) extends Event {

  private var remaining: Int = events.size
  private val lock = new Lock
  
  for (e <- events) {
    e.addCallback(_ => {
      val rem = lock.synchronized[Int] {
        remaining -= 1
        remaining
      }
      if (rem == 0) complete
    })
  }
}

object EventGroup {
  implicit def seqev[E<:Event](evs:Seq[E]) = new {
    def group = new EventGroup[E](evs)
  }
}
