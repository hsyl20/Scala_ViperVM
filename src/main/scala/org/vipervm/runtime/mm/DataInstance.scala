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

package org.vipervm.runtime.mm

import org.vipervm.platform.MemoryNode

/** Instance of a data */
abstract class DataInstance { self =>
  val typ:VVMType
  val meta:MetaData
  val repr:Repr
  val properties:ReprProperties
  val storage:Storage

  /**
   * Indicate whether a data is available in a memory
   */
  def isAvailableIn(memory:MemoryNode):Boolean = {
    storage.views.forall(_.buffer.memory == memory)
  }

  def copy(typ:VVMType = self.typ, meta:MetaData = self.meta, repr:Repr = self.repr, properties:ReprProperties = self.properties, storage:Storage = self.storage):DataInstance = {
    val (t,m,r,p,s) = (typ,meta,repr,properties,storage)
    new DataInstance {
      val typ = t
      val meta = m
      val repr = r
      val properties = p
      val storage = s
    }
  }
}

object DataInstance {
  def apply(t:VVMType, m:MetaData, r:Repr, p:ReprProperties, s:Storage) = {
    new DataInstance {
      val typ = t
      val meta = m
      val repr = r
      val properties = p
      val storage = s
    }
  }
}
