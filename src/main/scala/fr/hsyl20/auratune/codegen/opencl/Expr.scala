package fr.hsyl20.auratune.codegen.opencl


trait Expr {
   val typ: CType
   def +(v:Expr) = new OpAdd(this, v)
   def *(v:Expr) = new OpMul(this, v)
   def -(v:Expr) = new OpSub(this, v)
   def /(v:Expr) = new OpDiv(this, v)

   def <(v:Expr) = new OpInf(this,v)
}

abstract class Op(val a:Expr, val b:Expr) extends Expr {
   val csym: String

   if (a.typ != b.typ) {
      val msg = "Error: OpAdd: incompatible types (%s and %s)\n".format(a.typ, b.typ)
      throw new Exception(msg)
   }

   val typ = a.typ
}

class OpAdd(a:Expr, b:Expr) extends Op(a,b) {
   val csym = "+"
}

class OpSub(a:Expr, b:Expr) extends Op(a,b) {
   val csym = "-"
}

class OpMul(a:Expr, b:Expr) extends Op(a,b) {
   val csym = "*"
}

class OpDiv(a:Expr, b:Expr) extends Op(a,b) {
   val csym = "/"
}

class OpInf(a:Expr, b:Expr) extends Op(a,b) {
   val csym = "<"

   override val typ = CBoolean
}

