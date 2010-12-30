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

package demos.codegen

import fr.hsyl20.vipervm.codegen.opencl._

class CodeGen {
   val code = new CLCode {
      'a :- CFloat := "1.0"
      'b :- CFloat := "2.0"
      'c := 'a + 'b
   }

   println(code)
}


object Main {
   def main(args:Array[String]): Unit = {
      new CodeGen     
   }
}
