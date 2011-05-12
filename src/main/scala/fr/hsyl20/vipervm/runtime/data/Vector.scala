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

package fr.hsyl20.vipervm.runtime.data

import fr.hsyl20.vipervm.platform.{BufferView1D,MemoryNode}
import fr.hsyl20.vipervm.runtime.Data

class Vector(size:Long) extends Data {
  type ViewType = BufferView1D

  def allocate(memory:MemoryNode):BufferView1D = {
    val buffer = memory.allocate(size)
    new BufferView1D(buffer, 0, size)
  }
}
