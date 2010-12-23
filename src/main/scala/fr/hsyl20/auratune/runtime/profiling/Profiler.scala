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

package fr.hsyl20.auratune.runtime.profiling

/**
 * Allow registration of events with their associated date
 */
class Profiler {
  protected var _events:List[(ProfilingEvent,Long)] = Nil

  /**
   * Use this method to notify the profiler that an event has just happened
   */
  def notify(event:ProfilingEvent): Unit = synchronized {
    _events ::= (event -> System.currentTimeMillis)
  }

  /**
   * Retrieve list of (event,date)
   */
  def events = _events
}
