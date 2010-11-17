package fr.hsyl20.auratune.codegen.opencl

class IndexedVar(val base:Variable, val index:Variable) extends Variable(base.typ^,base.space) {
   override val id = "%s[%s]".format(base.id, index.id)
}

class Variable(val typ:CType, val space:AddressSpace) extends Expr {
   val id = "v" + Variable.getId

   def apply(v:Variable): IndexedVar = new IndexedVar(this, v)
}

object Variable {
   private var idc = 0
   def getId : Int = {
      idc += 1
      idc
   }

   def apply(typ:CType, space:AddressSpace = DefaultSpace): Variable = {
      new Variable(typ, space)
   }
}

class AddressSpace(val name:String)
object DefaultSpace extends AddressSpace("")

object AddressSpace {
   def apply(s:String) = new AddressSpace(s)
}
