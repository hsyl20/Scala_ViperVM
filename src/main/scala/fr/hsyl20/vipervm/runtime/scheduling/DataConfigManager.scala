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

import fr.hsyl20.vipervm.platform.MemoryNode
import fr.hsyl20.vipervm.runtime.Data

/**
 * Manage data configurations
 */
class DataConfigManager {

  protected var configs:List[DataConfig] = Nil
  protected var mems:Map[MemoryNode,Set[Data]] = Map.empty

  /**
   * Register a data configuration
   */
  def register(config:DataConfig):Unit = 
    configs ::= config

  /**
   * Unregister a data configuration
   */
  def unregister(config:DataConfig):Unit =
    configs.remove(_ == config)

  /**
   * Indicate that a data has been stored in a memory node
   */
  def put(data:Data,memory:MemoryNode):Unit = {
    val ds = mems.getOrElse(memory, Set.empty)
    mems.update(memory, ds + data)
  }

  /**
   * Indicate that a data has been removed from a memory node
   */
  def remove(data:Data,memory:MemoryNode):Unit = {
    val ds = mems.getOrElse(memory, Set.empty)
    mems.update(memory, ds - data)
  }

  /**
   * Get active data configs (i.e. data configs that are valid on the node)
   */
  def activeConfigs(memory:MemoryNode):Seq[DataConfig] = {
    val ds = mems.getOrElse(memory, Set.empty)
    configs.filter(config =>
      !config.excluded.contains(memory) &&
      (config.included.isEmpty || config.included.contains(memory)) &&
      config.dataSet.subsetOf(ds))
  }

  /**
   * Retrieve data that aren't used by any data config on a node
   */
  def unusedOn(memory:MemoryNode):Set[Data] = {
    val ds = mems.getOrElse(memory, Set.empty)
    val cs = activeConfigs(memory)
    val used = (Set.empty[Data] /: cs)((a,b) => a union b.dataSet)
    ds -- used
  }
}
