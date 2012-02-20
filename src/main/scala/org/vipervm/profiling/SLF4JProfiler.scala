/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.profiling

import grizzled.slf4j.Logging

class SLF4JProfiler extends Profiler with Logging {

  def reactions(e:ProfilingEvent):Unit = e match {
    case DataTransferStart(data,dt) => info("Starting transfer of %s from memory %s to memory %s using link %s".format(data,dt.source.buffer.memory,dt.target.buffer.memory,dt.link))
    case DataTransferEnd(data,dt) => info("Transfer of %s from memory %s to memory %s using link %s completed".format(data,dt.source.buffer.memory,dt.target.buffer.memory,dt.link))
    case TaskCompleted(task, proc) => info("Task %s completed by processor %s".format(task,proc))
    case TaskStart(task,kernel,proc) => info("Execution of task %s started by processor %s using kernel %s".format(task,proc,kernel))
    case TaskAssigned(task, proc) => info("Task %s assigned to processor %s".format(task,proc))
  }
}
