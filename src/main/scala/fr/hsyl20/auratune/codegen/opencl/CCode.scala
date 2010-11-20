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
      val rv = compute(e)
      if (rv.typ != v.typ)
         throw new Exception("Invalid type")
      if (v.space == DefaultSpace)
         append("%s %s = %s;\n".format(v.typ.id, v.id, rv.id))
      else
         append("__%s %s %s = %s;\n".format(v.space.name, v.typ.id, v.id, rv.id))
      addSymbol(v.id, v)
      v
   }

   def declareInitRaw(v:Variable, s:String): Variable = {
      if (v.space == DefaultSpace)
         append("%s %s = %s;\n".format(v.typ.id, v.id, s))
      else
         append("__%s %s %s = %s;\n".format(v.space.name, v.typ.id, v.id, s))
      addSymbol(v.id, v)
      v
   }

   def compute(e:Expr): Variable = e match {
      case v: Variable => v
      case op: Op => {
         val (a,b) = (op.a, op.b)
         val a_var = compute(a)
         val b_var = compute(b)
         val op_var = Variable(op.typ)
         declareInitRaw(op_var, "%s %s %s".format(a_var.id, op.csym, b_var.id))
         op_var
      }
   }

}
