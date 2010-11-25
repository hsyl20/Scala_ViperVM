package fr.hsyl20.auratune.codegen.opencl


class CMap(f:CFunction, src:Variable, dest:Variable, size:Int) {

   if (f.arity != 1)
      throw new Exception("Function of arity 1 expected for map")

   val threadsCount = 512

   val mod = size % threadsCount

   val code = new CLCode {
      kernel("mymap", Global __ CFloat*, Global __ CFloat*, CInt) {
         case List(src, dest, size) =>
            'ggid := getGlobalId(0)
            cif('ggid < size) {
               val e = f(src('ggid))
               assign(dest('ggid), e)
            }
      }
   }

   override def toString = code.toString
}


object CMap {
   def apply(f:CFunction,src:Variable,dest:Variable,size:Int): CMap = {
      new CMap(f, src, dest,size)
   }
}

