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

/**
 * A DataState represents a configuration of Data
 */
class DataState {

  val releaseEvent: UserEvent = new UserEvent

  /**
   * Release data state on event completion
   */
  def releaseOn(event:Event): Unit = {
    event.addCallback(_ => release)
  }

  /**
   * Release the Data State
   *
   * Indicate that Data in this data state can be released, that
   * buffers can be moved or released, etc.
   */
  final def release: Unit = {
    if (!releaseEvent.test) {
      releaseEvent.trigger
    }
  }

  def addReleaseCallback(f:this.type=>Unit): Unit = {
    releaseEvent.addCallback(_ => f(this))
  }
}
