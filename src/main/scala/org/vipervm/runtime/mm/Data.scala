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

import org.vipervm.runtime.Runtime
import org.vipervm.platform.MemoryNode

class Data(runtime:Runtime) {

  def typ:Option[VVMType] = runtime.getDataType(this)

  def typ_=(typ:VVMType):Unit = runtime.setDataType(this,typ)

  def meta:Option[MetaData] = runtime.getDataMeta(this)

  def meta_=(meta:MetaData):Unit = runtime.setDataMeta(this,meta)

  def associate(instance:DataInstance):Unit = runtime.associateDataInstance(data,instance)
}
