package fr.hsyl20.auratune.codegen.opencl

import scala.collection.breakOut

class CLCode extends CCode 
   with Block
   with Scope
   with CLAddressSpaces
   with If
   with CLBuiltIn {

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
         val v = compute(e)
         getSymbol(s.name) match {
            case None => {
               val vv = Variable(v.typ)
               declareInit(vv, v)
               addSymbol(s.name,vv)
            }
            case Some(vv:Variable) => assign(vv,v)
            case _ => throw new Exception("Invalid symbol")
         }
      }

      def :- (t:CType): Variable = {
         getSymbol(s.name) match {
            case Some(v:Variable) => if (v.typ != t) error("Invalid ascription") else v
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
   }

   private def checkVarSymbol(v:Variable): Any = v match {
      case v:IndexedVar => {
         checkSymbol(v.index.id)
         checkSymbol(v.base.id)
      }
      case v:Variable => checkSymbol(v.id)
   }

   implicit def var2assign(v:Variable) = new {
      def := (e:Expr) = assign(v,e)
   }

   def assign(v:Variable,e:Expr): Unit = {
      checkVarSymbol(v)
      val rv = compute(e)
      if (rv.typ != v.typ)
         throw new Exception("Invalid types in assignment (%s and %s)".format(v.typ, rv.typ))
      append("%s = %s;\n".format(v.id,rv.id))
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
         val r = compute(f(vars :_*))
         if (r.typ != f.returnType)
            throw new Exception("Invalid return type")
         append("return %s;\n".format(r.id))
      }
      append("}\n")
   }

   def kernel(name:String, args:VarType*)(body: PartialFunction[Seq[Variable],Unit]): Unit = {
      val vars: List[Variable] = args.map(v => new Variable(v.typ, v.space))(breakOut)
      val as = vars.map(v =>
         if (v.space == DefaultSpace)
            "%s %s".format(v.typ.id, v.id)
         else
            "__%s %s %s".format(v.space.name, v.typ.id, v.id)
         ).mkString(", ")
      append("__kernel void %s(%s) {\n".format(name, as))
      indent {
         vars.foreach(v => addSymbol(v.id, v))
         if (!body.isDefinedAt(vars))
            throw new Exception("Invalid kernel arguments")
         body(vars)
      }
      append("}\n")
   }

   def call(f:CFunction, args:Array[Expr]): Variable = {
      val args2 = args.map(compute(_))
      val v = Variable(f.returnType)
      val as = args2.map(_.id).mkString(", ")
      val s = "%s(%s)".format(f.id, as)
      declareInitRaw(v, s)
      v
   }
}
