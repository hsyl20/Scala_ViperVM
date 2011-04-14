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

/**
 * Default implementation for functional scheduler
 */
class DefaultFunctionalScheduler extends ActorFunctionalScheduler {

  def onInstanceEnqueued(instance:FunctionalKernelInstance): Unit = {

    /* Wait for input data to be ready */
    //TODO
  }

  def onInputDataReady(instance:FunctionalKernelInstance): Unit = {

    /* Select kernels that can handle the input data */
    //TODO

    /* Make the data configuration on some node that can execute at least one of the kernels */
    //TODO
  }

  def onDataConfigReady(instance:FunctionalKernelInstance,configs:Seq[DataConfigInstance]): Unit = {
     
    /* Select the node and the kernel that will be executed */
    //TODO

    /* Release unnecessary data configuration instances */
    //TODO

    /* Schedule execution */
    //TODO
  }

  def onInstanceExecuted(instance:FunctionalKernelInstance,config:DataConfigInstance): Unit = {
    /* Set output data after execution */
    //TODO

    /* Release data configuration instance */
    //TODO
  }
}
