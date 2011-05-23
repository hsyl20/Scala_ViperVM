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
import org.vipervm.platform.{EventGroup,Platform,MemoryNode,Processor}

import scala.collection.mutable.Map

/**
 * Default implementation for functional scheduler
 */
class DefaultFunctionalScheduler(platform:Platform) extends ActorFunctionalScheduler {

  protected val configs:Map[DataConfig,List[Task]] = Map.empty
  protected val confManager:DataConfigManager = new DataConfigManager

  override def onTaskSubmitted(task:Task): Unit = {

    /* Wait for input data to be ready */
    /*val evGrp = new EventGroup(task.input.map(_.computedEvent))
    evGrp willTrigger {
      this ! InputDataReady(task)
    }*/
  }

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

    /* Make the data configuration on some node that can execute at least one of the kernels */
    val dataConfig = new DataConfig {
      val dataSet = task.input.toSet | task.output.toSet
    }

    /* store and require this dataconfig for this task */
    storeDataConfig(dataConfig, task)
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

  def storeDataConfig(config:DataConfig,task:Task): Unit = {
    val old = configs.getOrElse(config, Nil)
    configs.update(config, task :: old)

    //TODO: require dataconfig
  }
}
