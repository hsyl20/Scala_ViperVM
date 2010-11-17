package fr.hsyl20.auratune.codegen.opencl


class CMap(f:CFunction, n:Expr, src:Variable, dest:Variable) {

   if (f.arity != 1)
      throw new Exception("Function of arity 1 expected for map")

   val code = new CLCode {
      val nvar = compute(n)
      val ggid = declareInitRaw(Variable(CInt), "get_global_id(0)")
      cif(nvar < ggid) {
         val args = List(src(ggid))
         val t = compute(f(args))
         assign(dest(ggid), t)
      }
   }
}


object CMap {
   def apply(f:CFunction,n:Expr,src:Variable,dest:Variable): CMap = {
      new CMap(f, n, src, dest)
   }
}

