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

package org.vipervm.runtime

import org.vipervm.platform.{Event,MemoryNode,Processor}
import org.vipervm.runtime.mm.config._
import org.vipervm.runtime.mm.{Data,DataInstance}

trait KernelPrototype {

  /* Compute from parameters the memory configuration required to compute result */
  def execConf(params:Seq[Data],memory:MemoryNode,proc:Processor): DataConfig

}


