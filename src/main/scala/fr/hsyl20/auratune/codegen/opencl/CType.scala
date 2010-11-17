package fr.hsyl20.auratune.codegen.opencl


abstract class CType {
   val id:String

   def ^ : CType = throw new Exception("Variable cannot be dereferenced")

   def * : CType = CPtr(this)
}


case class CPtr(t:CType) extends CType {
   val id = "%s*".format(t.id)

   override def ^ : CType = t
}
