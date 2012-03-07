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

package org.vipervm.library

import org.vipervm.platform.Processor
import org.vipervm.runtime.{Function,MetaKernel}
import org.vipervm.runtime.mm.VVMType
import org.vipervm.runtime.mm.Repr

class LibraryV2(val functions:Seq[FunctionRef]) {

  def filter(cond:FunctionRef => Boolean):LibraryV2 = {
    new LibraryV2(functions.filter(cond))
  }

  def filterName(name:String) = filter(_.name == name)
    
  def filterParamType(index:Int,typ:VVMType) = filter(_.paramTypes(index) == typ)

  def filterParamRepr(index:Int,repr:Repr) = filter(_.paramRepr(index) == repr)

  def filterResultType(typ:VVMType) = filter(_.resultType == typ)

  def filterResultRepr(repr:Repr) = filter(_.resultRepr == repr)

  def fitlerProcessor(proc:Processor) = filter(_.kernel.canExecuteOn(proc))
}

case class FunctionRef(
  name:String,
  paramTypes:Seq[VVMType],
  paramRepr:Seq[Repr],
  resultType:VVMType,
  resultRepr:Repr,
  kernel:MetaKernel
)
