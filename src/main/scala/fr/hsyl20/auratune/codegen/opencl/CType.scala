package fr.hsyl20.auratune.codegen.opencl


sealed abstract class CType {
   val id:String

   def ^ : CType = throw new Exception("Variable cannot be dereferenced")

   def * : CType = CPtr(this)
}

case object CBoolean extends CType {
   val id = "int"
}

case object CFloat extends CType {
   val id = "float"
}

case object CDouble extends CType {
   val id = "double"
}

case object CInt extends CType {
   val id = "int"
}

case class CPtr(t:CType) extends CType {
   val id = "%s*".format(t.id)

   override def ^ : CType = t
}
