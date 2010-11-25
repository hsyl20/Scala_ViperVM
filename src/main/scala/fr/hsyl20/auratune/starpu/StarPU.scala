package fr.hsyl20.auratune.starpu

import scala.annotation.tailrec
/*
object StarPU {

   @tailrec
   def compile(cmds:List[Command], code: String = ""): String = {

      if (cmds.isEmpty) ""
      else {
         val c = cmds.head match {
            /* Already registered data provided by user code */
            case Registered(_) => ""

            /* Data that need to be allocated (lazily) */
            case Alloc(d) => d match {
               case Matrix2D(t,w,h) => "starpu_variable_data_register(&%s, -1, -1, %d); ".format(d.id, typeSize(t) * w * h)
            }
            
         }
         compile(cmds.tail, code + c)
      }
   }

}*/
