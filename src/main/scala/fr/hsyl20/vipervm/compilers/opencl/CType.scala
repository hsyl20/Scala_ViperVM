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

package fr.hsyl20.vipervm.codegen.opencl


abstract class CType {
   val id:String

   def ^ : CType = throw new Exception("Variable cannot be dereferenced")

   def * : CType = CPtr(this)
}


case class CPtr(t:CType) extends CType {
   val id = "%s*".format(t.id)

   override def ^ : CType = t
}
