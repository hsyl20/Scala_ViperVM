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
import scala.actors.Actor
import scala.actors.Actor._

/**
 * Functional scheduler implemented with actors
 */
abstract class ActorFunctionalScheduler extends Actor with FunctionalScheduler {

  /**
   * Signal to the scheduler that an instance has been enqueued
   */
  override def enqueue(instance:FunctionalKernelInstance): Unit = {
    this ! InstanceEnqueued(instance)
  }

  def act() {
    loop {
      react {
        case InstanceEnqueued(instance) => onInstanceEnqueued(instance)
        case InputDataReady(instance) => onInputDataReady(instance)
        case DataConfigReady(instance,configs) => onDataConfigReady(instance,configs)
        case InstanceExecuted(instance,config) => onInstanceExecuted(instance,config)
      }
    }
  }

  /**
   * Called when an instance has been enqueued
   */
  def onInstanceEnqueued(instance:FunctionalKernelInstance): Unit

  /**
   * Called when input data are ready
   */
  def onInputDataReady(instance:FunctionalKernelInstance): Unit

  /**
   * Called when data configuration for this kernel is available in at least one memory
   */
  def onDataConfigReady(instance:FunctionalKernelInstance,configs:Seq[DataConfigInstance]): Unit

  /**
   * Called when kernel execution has completed using the specified data configuration
   */
  def onInstanceExecuted(instance:FunctionalKernelInstance,config:DataConfigInstance): Unit

  /** Actor messages */
  protected case class InstanceEnqueued(instance:FunctionalKernelInstance)
  protected case class InputDataReady(instance:FunctionalKernelInstance)
  protected case class DataConfigReady(instance:FunctionalKernelInstance, configs:Seq[DataConfigInstance])
  protected case class InstanceExecuted(instance:FunctionalKernelInstance, config:DataConfigInstance)
}


