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

class Data(dataManager:DataManager) {

  def typ:Option[VVMType] = dataManager.getType(this)

  def typ_=(typ:VVMType):Unit = dataManager.setType(this,typ)

  def meta:Option[MetaData] = dataManager.getMetaData(this)

  def meta_=(meta:MetaData):Unit = dataManager.setMetaData(this,meta)
}

case class TypedData(data:Data,typ:VVMType)
case class TypedDataWithMetaData(data:Data,typ:VVMType,meta:MetaData)
case class DataReprInstance(data:Data,repr:Repr,instance:DataInstance)

sealed abstract class AllocationFailure
case object OutOfMemoryFailure extends AllocationFailure
case object DataRepresentationNotSupported extends AllocationFailure
