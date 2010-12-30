/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.runtime.profiling

import fr.hsyl20.vipervm.runtime._

/**
 * Events supported by the profiler
 */
abstract class ProfilingEvent
case class TaskSubmitted(task:Task) extends ProfilingEvent
case class TaskScheduled(task:ScheduledTask) extends ProfilingEvent
case class TaskStart(task:Task) extends ProfilingEvent
case class TaskEnd(task:Task) extends ProfilingEvent

case class DataTransfer(data:Data,from:MemoryNode,to:MemoryNode) extends ProfilingEvent
