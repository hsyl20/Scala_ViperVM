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

package fr.hsyl20.vipervm.runtime.scheduling

import fr.hsyl20.vipervm.runtime.FunctionalKernelInstance
import fr.hsyl20.vipervm.platform.{EventGroup,Platform,MemoryNode}

import scala.collection.mutable.Map

/**
 * Default implementation for functional scheduler
 */
class DefaultFunctionalScheduler(platform:Platform) extends ActorFunctionalScheduler {

  protected val configs:Map[DataConfig,List[FunctionalKernelInstance]] = Map.empty

  def onInstanceEnqueued(instance:FunctionalKernelInstance): Unit = {

    /* Wait for input data to be ready */
    val evGrp = new EventGroup(instance.input.map(_.computedEvent))
    evGrp willTrigger {
      this ! InputDataReady(instance)
    }
  }

  def onInputDataReady(instance:FunctionalKernelInstance): Unit = {

    /* Select kernels that can handle the input data */
    //TODO

    /* Select nodes that can execute these kernels */
    //TODO

    /* Make the data configuration on some node that can execute at least one of the kernels */
    val dataConfig = new DataConfig {
      val dataSet = instance.input.toSet | instance.output.toSet
    }

    /* store and require this dataconfig for this instance */
    storeDataConfig(dataConfig, instance)
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

  def onInstanceExecuted(instance:FunctionalKernelInstance,config:DataConfig,memory:MemoryNode): Unit = {
    /* Set output data after execution */
    //TODO

    /* Release data configuration instance */
    //TODO
  }

  def storeDataConfig(config:DataConfig,instance:FunctionalKernelInstance): Unit = {
    val old = configs.getOrElse(config, Nil)
    configs.update(config, instance :: old)

    //TODO: require dataconfig
  }
}
