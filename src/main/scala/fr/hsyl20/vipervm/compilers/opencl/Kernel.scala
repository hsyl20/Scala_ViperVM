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

package fr.hsyl20.vipervm.compilers.opencl

import fr.hsyl20.vipervm.compilers._
import fr.hsyl20.vipervm.codegen.opencl.CType


object AddressSpace extends Enumeration {
  type AddressSpace = Value
  val Global,Local,Constant,Private = Value
}

case class Parameter(name:String,typ:CLType)

case class CLType(typ:CType,addressSpace:AddressSpace.AddressSpace)


class Kernel {
  import AddressSpace._

  private var params:List[Parameter] = Nil
  private var code = new StringBuffer

  def addParam(name:String,typ:CLType): Parameter = {
    val p = Parameter(name,typ)
    params ::= p
    p
  }

  private def append(s:String) = code.append(s)

}
