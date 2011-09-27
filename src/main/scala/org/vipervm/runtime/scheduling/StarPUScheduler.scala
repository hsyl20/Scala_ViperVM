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

package org.vipervm.runtime.scheduling

/**
 * This scheduler mimic the one found in StarPU Runtime System
 *
 * There is one queue per compute unit. When a task is submitted
 * to the scheduler, it puts it in one of these queues. For each device,
 * there is an associated thread that selects data that need to be
 * transferred to a memory node associated to the device for some of
 * the tasks to be executed.
 */
/*class StarPUScheduler extends Scheduler {
  
}*/
