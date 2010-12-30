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

trait If extends CCode with Block with Scope {

   def cif(e:Expr)(body: => Unit): Unit = {
      if (e.typ != CBoolean)
         throw new Exception("Boolean type required")

      append("if (%s) {\n".format(expr(e)))
      indent(body)
      append("}\n")
   }
}
