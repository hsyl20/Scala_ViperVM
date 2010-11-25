package fr.hsyl20.auratune.codegen.opencl

trait If extends CCode with Block with Scope {

   def cif(e:Expr)(body: => Unit): Unit = {
      if (e.typ != CBoolean)
         throw new Exception("Boolean type required")

      append("if (%s) {\n".format(expr(e)))
      indent(body)
      append("}\n")
   }
}
