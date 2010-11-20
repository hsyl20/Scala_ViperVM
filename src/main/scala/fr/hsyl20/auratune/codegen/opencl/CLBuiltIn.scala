package fr.hsyl20.auratune.codegen.opencl

trait CLBuiltIn extends CCode {
   def getGlobalId(x:Int): Variable = {
      declareInitRaw(Variable(CInt), "get_global_id(%d)".format(x))
   }
}
