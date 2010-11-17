package fr.hsyl20.auratune.codegen.opencl

trait Block {
   private var code: String = ""
   private var indentv: Int = 0

   def scopeIn: Unit = {}
   def scopeOut: Unit = {}

   def indent[A](body: =>A): A = {
      indentv += 1
      scopeIn
      val a = body
      scopeOut
      indentv -= 1
      a
   }

   def append(s:String): Unit = {
      code += "   " * indentv + s
   }

   override def toString = code
}
