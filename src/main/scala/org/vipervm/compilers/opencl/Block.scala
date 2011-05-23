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

package org.vipervm.codegen.opencl

trait Block {
   protected var code: String = ""
   private var indentv: Int = 0

   def scopeIn: Unit = {}
   def scopeOut: Unit = {}

   def indent[A](body: =>A): A = {
      indentv += 1
      scopeIn
      val a = body
      scopeOut
      indentv -= 1
      a
   }

   def append(s:String): Unit = {
      code += "   " * indentv + s
   }

   override def toString = code
}
