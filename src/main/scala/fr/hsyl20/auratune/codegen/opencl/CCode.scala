package fr.hsyl20.auratune.codegen.opencl

import scala.collection.immutable.Stack
import scala.collection._

class CCode {
   private var code: String = ""
   private var indentv: Int = 0
   private var symbols: Stack[mutable.Map[String,Any]] = new Stack

   var warnOnly: Boolean = true

   symbols = symbols.push(new mutable.HashMap)

   private def warning(s:String): Unit = {
      if (warnOnly)
         println("WARNING: %s".format(s))
      else
         throw new Exception(s)
   }


   private def sym(s:String, a:Any): Unit = {
      val m = symbols.head
      if (m.isDefinedAt(s)) warning("Symbol \"%s\" already declared".format(s))
      m += (s -> a)
   }

   private def checksym(s:String, ss:Stack[mutable.Map[String,Any]] = symbols): Option[Any] = {
      val ret = ss.headOption flatMap (_.get(s) orElse checksym(s, ss.tail))
      
      if (ret == None && ss == symbols)
         warning("Symbol \"%s\" not declared".format(s))
      
      ret
   }

   private def checksym_var(v:Variable): Any = v match {
      case v:IndexedVar => checksym(v.index.id) ; checksym(v.base.id)
      case v:Variable => checksym(v.id)
   }

   implicit def var2assign(v:Variable) = new {
      def := (e:Expr) = assign(v,e)
   }

   def append(s:String): Unit = {
      code += "   "*indentv
      code += s
   }

   def indent[A](body: =>A): A = {
      indentv += 1
      symbols = symbols.push(new mutable.HashMap)
      val a = body
      symbols = symbols.pop
      indentv -= 1
      a
   }

   def declare(v:Variable): Variable = {
      v.space match {
         case None => append("%s %s;\n".format(v.typ.id, v.id))
         case Some(s) => append("__%s %s %s;\n".format(s, v.typ.id, v.id))
      }
      sym(v.id, v)
      v
   }

   def declareInit(v:Variable, e:Expr): Variable = {
      val rv = compute(e)
      if (rv.typ != v.typ)
         throw new Exception("Invalid type")
      v.space match {
         case None => append("%s %s = %s;\n".format(v.typ.id, v.id, rv.id))
         case Some(s) => append("__%s %s %s = %s;\n".format(s, v.typ.id, v.id, rv.id))
      }
      sym(v.id, v)
      v
   }

   def declareInitRaw(v:Variable, s:String): Variable = {
      v.space match {
         case None => append("%s %s = %s;\n".format(v.typ.id, v.id, s))
         case Some(sp) => append("__%s %s %s = %s;\n".format(sp, v.typ.id, v.id, s))
      }
      sym(v.id, v)
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

   def assign(v:Variable,e:Expr): Unit = {
      checksym_var(v)
      val rv = compute(e)
      append("%s = %s;\n".format(v.id,rv.id))
   }

   def cif(e:Expr)(body: => Unit): Unit = {
      val v = compute(e)
      if (v.typ != CBoolean)
         throw new Exception("Boolean type required")

      append("if (%s) {\n".format(v.id))
      indent(body)
      append("}")
   }


   def declareFun(f:CFunction): Unit = {
      val vars = f.args.map(t => new Variable(t.typ, t.space))
      val argss = vars.map(v => v.space match {
            case None => "%s %s".format(v.typ.id, v.id)
            case Some(s) => "__%s %s %s".format(s, v.typ.id, v.id)
         }).mkString(", ")

      append("%s %s(%s) {\n".format(f.returnType.id, f.id, argss))
      indent {
         vars.foreach(v => sym(v.id, v))
         val r = compute(f(vars))
         if (r.typ != f.returnType)
            throw new Exception("Invalid return type")
         append("return %s;\n".format(r.id))
      }
      append("}\n")
   }

   def kernel(name:String, args:Variable*)(body: =>Unit): Unit = {
      val as = args.map(v => v.space match {
            case None => "%s %s".format(v.typ.id, v.id)
            case Some(s) => "__%s %s %s".format(s, v.typ.id, v.id)
         }).mkString(", ")
      append("__kernel void %s(%s) {\n".format(name, as))
      indent {
         args.foreach(v => sym(v.id, v))
         body
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

   override def toString = code
}
