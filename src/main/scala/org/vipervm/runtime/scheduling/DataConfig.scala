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

import org.vipervm.platform.MemoryNode
import org.vipervm.runtime.Data

/**
 * Group of Data to access with the specified access mode.
 *
 * Support memory node constraints (include, exclude) and piorities
 *
 * @param data Data and their associated access modes
 */
abstract class DataConfig {

  /**
   * if "included" is not Nil, the data configuration must only be
   * configured on a memory node contained in it. Otherwise any
   * memory node in the platform can be used.
   */
  val included:Seq[MemoryNode] = Nil

  /**
   * Memory nodes that must not be used
   */
  val excluded:Seq[MemoryNode] = Nil

  /**
   * Memory node priorities. 0 is default. Negative is lower priority and positive is higher priority
   */
  val priorities: Map[MemoryNode, Float] = Map.empty

  /** 
   * Required data
   *  - Non existing data will be freshly allocated.   
   *  - Existing data will be transferred
   *  - Duplicated data will be duplicated if necessary
   */
  val dataSet:Set[Data]
}
