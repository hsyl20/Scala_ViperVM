package fr.hsyl20.auratune.codegen.opencl

object CFunction {
   private var idc = 0
   def getId: String = {
      idc += 1
      "fun" + idc
   }

   def apply(rt:CType, args:Variable*)(body:PartialFunction[List[Variable],Expr]): CFunction = {
      new CFunction(rt, args.toList, body)
   }
}

class CFunction(val returnType:CType, val args:List[Variable], body:PartialFunction[List[Variable],Expr]) {
   val id = CFunction.getId
   val arity = args.size

   def apply(args:List[Variable]): Expr = {
      if (!body.isDefinedAt(args))
         throw new Exception("Invalid function body (check argument count)")
      body(args)
   }
}
