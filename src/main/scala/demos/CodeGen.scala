package demos.codegen

import fr.hsyl20.auratune.codegen.opencl._

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
