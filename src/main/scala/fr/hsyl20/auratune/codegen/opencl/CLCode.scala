package fr.hsyl20.auratune.codegen.opencl

import scala.collection.breakOut

class CLCode extends CCode 
   with Block
   with Scope
   with CLAddressSpaces
   with If
   with CLBuiltIn {

   private def checkVarSymbol(v:Variable): Any = v match {
      case v:IndexedVar => {
         checkSymbol(v.index.id)
         checkSymbol(v.base.id)
      }
      case v:Variable => checkSymbol(v.id)
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

}
