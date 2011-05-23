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

package org.vipervm.runtime.scheduling

import org.vipervm.runtime.Task
import org.vipervm.platform.{Platform,MemoryNode,Processor}

import scala.collection.mutable.Map

/**
 * Default implementation for functional scheduler
 */
class DefaultFunctionalScheduler(platform:Platform) extends ActorFunctionalScheduler with TaskDataWait with DataManager {

  protected val configs:Map[DataConfig,List[Task]] = Map.empty


  override def onTaskCompleted(task:Task,proc:Processor,memory:MemoryNode): Unit = {
    /* Set output data after execution */
    //TODO

    /* Release data configuration task */
    //TODO
  }


  def onInputDataReady(task:Task): Unit = {

    /* Select kernels that can handle the input data */
    //TODO

    /* Select nodes that can execute these kernels */
    //TODO

    /* store and require this dataconfig for this task */
  }

  def onDataConfigReady(config:DataConfig,memory:MemoryNode): Unit = {
    
    /* Select processor */
    val procs = platform.processorsThatCanWorkIn(memory)
    //TODO: only schedule on idle processor
    val proc = procs.head

    /* Select the kernel that will be executed */
    val fkernels = configs(config)
    val fkernel = fkernels.filter(_.kernel.kernel.canExecuteOn(proc)).head

    /* Retrieve kernel data in this memory */

    /* Schedule execution */
    //proc.execute(fkernel.kernel
    //TODO
  }

}
