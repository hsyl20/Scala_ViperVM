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
/*
import scala.collection.mutable.{Map,HashMap}
import fr.hsyl20.vipervm.runtime._
import fr.hsyl20.vipervm.runtime.EventGroup._

class DefaultDataScheduler(runtime:Runtime) {

  protected var waitingConfigs: List[DefaultDataConfigEvent] = Nil

  protected var dataStates: Map[Data,Data.State] = Map.empty

  /**
   * Return available memory nodes for the given configuration
   */
  protected def availableNodes(config:DataConfig):Seq[MemoryNode] = {
    val included = if (config.included.isEmpty) runtime.platform.memoryNodes else config.included
    included.filterNot(config.excluded.contains _)
  }

  /**
   * Check that the given data configuration is compatible with
   * active data configurations
   *
   * @return true if compatible, false otherwise
   */
  protected def checkCompatibility(config:DataConfig): Boolean = {
    import Data._
    import AccessMode._

    val (ds,modes) = config.data.unzip

    /* Get current data states. Unused data are Ready */
    val currentStates = ds.map(dataStates.getOrElse(_,Data.Ready))

    /* Check compatibility with access modes */
    val checks = for (cm <- (currentStates zip modes)) yield {
      cm match {
        case (Ready,_) => true
        case (Shared(_),ReadOnly) => true
        case _ => false
      }
    }

    checks forall (_ == true)
  }

  /**
   * Ask for a data configuration to be set up
   */
  def configure(config:DataConfig): DataConfigEvent = {

    /* Prepare the event that will be returned */
    val configEvent = new DefaultDataConfigEvent(config)

    /* Check if this state is compatible with the current ones */
    if (!checkCompatibility(config)) {
      waitingConfigs ::= configEvent
    }
    else {
      activate(configEvent)
    }

    configEvent
  }

  /**
   * Activate data configuration
   */
  protected def activate(configEvent:DefaultDataConfigEvent): Unit = {
    val config = configEvent.config

    /* Memory nodes to choose from */
    val nodes = availableNodes(config)

    /* Select memory node */
    val node = selectMemoryNode(config, nodes)
    configEvent.memNode = Some(node)

    /* Allocate buffers */
    val unallocated = unallocatedData(config,node)
    for (d <- unallocated) {
      /* Allocate buffers */
      d.allocate(node)

      /* If there is not enough estimated free memory or
       * if allocation fails, we need to remove buffers.
       * This may involve data transfer to host and may
       * not be possible now 
       */
      //TODO
    }

    /* Update buffers */
    val invalids = invalidData(config,node)

    //TODO: fix this to make the data scheduler work
    /*val syncTransfers = invalids.flatMap(d => {
      /* Get synchronization sources */
      val srcs = d.syncSources(node)

      /* Select source */
      val src = selectSyncSource(d, node, srcs)

      /* Start data copy */
      //TODO
      //d.sync(node, src)
    })

    /* Set callback to trigger the data configuration event once
     * every data transfer event has completed
     */
    val group = syncTransfers.map(_.event).group
    group.addCallback(_ => triggerDataConfigEvent(configEvent))
    */

    /* Add the data configuration to the list of active configuration */
    //TODO

    /* Update data states */
    //TODO

    /* Add a callback for the scheduler to be notified when
     * the active data configuration is released
     */
    config.addReleaseCallback(releaseConfig _)
  }


  /**
   * Select a source to perform buffer synchronization from for data
   *
   * Default is to select the first one in sources
   */
  protected def selectSyncSource(data:Data,node:MemoryNode,sources:Seq[MemoryNode]): MemoryNode = {
    sources.head
  }

  /**
   * Give a score for the datastate on the given memory node
   *
   * This higher it is, the most likely the data configuration will
   * be set on the memory node
   */
  protected def score(config:DataConfig, memoryNode:MemoryNode):Float = {
    val invalid = allInvalidData(config,memoryNode)
    val sizeToTransfer = invalid.map(_.sizeOn(memoryNode)).sum

    sizeToTransfer.toFloat * (-0.001f) + 10.0f * config.priorities.getOrElse(memoryNode, 0)
  }

  /**
   * Return the list of data from the data configuration that are
   * either not allocated on the given memory node, or allocated
   * but in a invalid state
   */
  protected def allInvalidData(config:DataConfig, memoryNode:MemoryNode):Seq[Data] = {
    config.data.map(_._1).flatMap(d => d.status(memoryNode) match {
        case None //unallocatedData
         |   Some(Data.Invalid) => Some(d) //invalidData
        case _ => None
    })
  }

  /**
   * Return the list of data from the data configuration that have
   * an allocated buffer on the given memory node but not in a
   * valid state
   */
  protected def invalidData(config:DataConfig, memoryNode:MemoryNode): Seq[Data] = {
    config.data.map(_._1).flatMap(d => d.status(memoryNode) match {
        case Some(Data.Invalid) => Some(d)
        case _ => None
    })
  }

  /**
   * Return data from the data configuration that have no allocated
   * buffer on the given memory node
   */
  protected def unallocatedData(config:DataConfig, memoryNode:MemoryNode):Seq[Data] = {
    config.data.map(_._1).flatMap(d => d.status(memoryNode) match {
        case None => Some(d)
        case _ => None
    })
  }

  /**
   * Select the best memory node for the given data configuration
   *
   * By default, memory node with best score is selected
   */
  protected def selectMemoryNode(config:DataConfig, nodes:Seq[MemoryNode]): MemoryNode = {
    (nodes zip nodes.map(m => score(config,m))).sortBy(_._2).last._1
  }

  /**
   * Indicate that a data configuration is no longer required
   */
  protected def releaseConfig(config:DataConfig): Unit = {
    import Data._

    /* Update data states */
    for (d <- config.data.map(_._1)) { dataStates(d) match {
      case Shared(n) if n > 1 => dataStates.update(d, Shared(n-1))
      case Shared(_)
       |   Exclusive => dataStates.remove(d)
      case _ => error("Invalid data state after data configuration release")
    }}

    /* Check for waiting configurations that may be enabled now */
    //TODO
  }

  /**
   * Callback called when data are ready on some node.
   *
   * Selected node is already set in configEvent
   * This callback should trigger configEvent
   */
  protected def triggerDataConfigEvent(configEvent:DefaultDataConfigEvent) {
    configEvent.trigger
  }
}

class DefaultDataConfigEvent(val config:DataConfig) extends UserEvent with DataConfigEvent {
  var memNode: Option[MemoryNode] = None

  def memoryNode = memNode.get
}

*/
