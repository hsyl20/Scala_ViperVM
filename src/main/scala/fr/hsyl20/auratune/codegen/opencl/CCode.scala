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

   implicit def sym2var(s:Symbol): Variable = {
      getSymbol(s.name) match {
         case Some(v:Variable) => v
         case _ => error("Invalid symbol %s".format(s.name))
      }
   }

   implicit def int2var(i:Int): Variable = {
      declareInitRaw(Variable(CInt), i.toString)
   }

   implicit def double2var(i:Double): Variable = {
      declareInitRaw(Variable(CDouble), i.toString)
   }

   implicit def float2var(i:Float): Variable = {
      declareInitRaw(Variable(CFloat), i.toString)
   }

   implicit def typ2vt(t:CType): VarType = new VarType(t, DefaultSpace)

   implicit def symex(s:Symbol) = new {
      def := (e:Expr): Unit = {
         getSymbol(s.name) match {
            case None => {
               val v = Variable(e.typ)
               declareInit(v, e)
               addSymbol(s.name,v)
            }
            case Some(v:Variable) => assign(v,e)
            case _ => throw new Exception("Invalid symbol")
         }
      }

      def := (raw:String): Unit = {
         getSymbol(s.name) match {
            case Some(v:Variable) => assignRaw(v,raw)
            case _ => throw new Exception("Invalid symbol")
         }
      }

      def :- (t:CType): Variable = {
         getSymbol(s.name) match {
            case Some(v:Variable) => if (v.typ != t) error("Invalid type ascription") else v
            case None => {
               val v = Variable(t)
               declare(v)
               addSymbol(s.name, v)
               v
            }
            case _ => error("Invalid symbol")
         }
      }

      def :- (vt:VarType): Variable = {
         getSymbol(s.name) match {
            case Some(v:Variable) => if (v.typ != vt.typ || v.space != vt.space) error("Invalid ascription") else v
            case None => {
               val v = Variable(vt.typ,vt.space)
               declare(v)
               addSymbol(s.name, v)
               v
            }
            case _ => error("Invalid symbol")
         }
      }
   }

   implicit def addex(a:AddressSpace) = new {
      def @@ (t:CType): VarType = new VarType(t,a)
      def apply (t:CType): VarType = new VarType(t,a)
   }

   implicit def var2assign(v:Variable) = new {
      def := (e:Expr) = assign(v,e)
      def := (s:String) = assignRaw(v,s)
   }

   def declareFun(f:CFunction): Unit = {
      val vars = f.args.map(t => new Variable(t.typ, t.space))
      val argss = vars.map(v =>
         if (v.space == DefaultSpace)
            "%s %s".format(v.typ.id, v.id)
         else
            "__%s %s %s".format(v.space.name, v.typ.id, v.id)
         ).mkString(", ")

      append("%s %s(%s) {\n".format(f.returnType.id, f.id, argss))
      indent {
         vars.foreach(v => addSymbol(v.id, v))
         val e = f(vars :_*)
         append("return %s;\n".format(expr(e)))
      }
      append("}\n")
   }

   def call(f:CFunction, args:Array[Expr]): Variable = {
      val v = Variable(f.returnType)
      val as = args.map(expr(_)).mkString(", ")
      val s = "%s(%s)".format(f.id, as)
      declareInitRaw(v, s)
      v
   }

}
