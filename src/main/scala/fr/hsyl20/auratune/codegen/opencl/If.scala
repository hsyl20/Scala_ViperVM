package fr.hsyl20.auratune.codegen.opencl

trait If extends CCode with Block with Scope {

   def cif(e:Expr)(body: => Unit): Unit = {
      val v = compute(e)
      if (v.typ != CBoolean)
         throw new Exception("Boolean type required")

      append("if (%s) {\n".format(v.id))
      indent(body)
      append("}")
   }
}
