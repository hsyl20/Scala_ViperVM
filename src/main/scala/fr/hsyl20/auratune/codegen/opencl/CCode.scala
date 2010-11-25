package fr.hsyl20.auratune.codegen.opencl


class VarType(val typ:CType, val space:AddressSpace) {
   def * = new VarType(typ*,space)
}

object VarType {
   def apply(typ:CType, space:AddressSpace = DefaultSpace) = new VarType(typ, space)

}


trait CCode extends Block with Scope {

   def declare(v:Variable): Variable = {
      if (v.space == DefaultSpace)
         append("%s %s;\n".format(v.typ.id, v.id))
      else
         append("__%s %s %s;\n".format(v.space.name, v.typ.id, v.id))
      addSymbol(v.id, v)
      v
   }

   def declareInit(v:Variable, e:Expr): Variable = {
      if (v.space == DefaultSpace)
         append("%s %s = %s;\n".format(v.typ.id, v.id, expr(e)))
      else
         append("__%s %s %s = %s;\n".format(v.space.name, v.typ.id, v.id, expr(e)))
      addSymbol(v.id, v)
      v
   }

   def assign(v:Variable,e:Expr): Unit = {
      append("%s = %s;\n".format(v.id,expr(e)))
   }


   def declareInitRaw(v:Variable, s:String): Variable = {
      if (v.space == DefaultSpace)
         append("%s %s = %s;\n".format(v.typ.id, v.id, s))
      else
         append("__%s %s %s = %s;\n".format(v.space.name, v.typ.id, v.id, s))
      addSymbol(v.id, v)
      v
   }

   def assignRaw(v:Variable, s:String): Unit = {
      append("%s = %s;\n".format(v.id, s))
   }

   def expr(e:Expr): String = e match {
      case v: Variable => v.id
      case op: Op => "(%s %s %s)".format(expr(op.a), op.csym, expr(op.b))
   }

}
