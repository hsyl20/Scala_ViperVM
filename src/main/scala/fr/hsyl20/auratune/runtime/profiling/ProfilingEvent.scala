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

import fr.hsyl20.auratune.runtime._

/**
 * Events supported by the profiler
 */
abstract class ProfilingEvent
case class TaskSubmitted(task:Task) extends ProfilingEvent
case class TaskScheduled(task:ScheduledTask) extends ProfilingEvent
case class TaskStart(task:Task) extends ProfilingEvent
case class TaskEnd(task:Task) extends ProfilingEvent

case class DataTransfer(data:Data,from:MemoryNode,to:MemoryNode) extends ProfilingEvent
