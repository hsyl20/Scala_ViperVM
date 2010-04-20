/*
**
**      \    |  | _ \    \ __ __| |  |  \ |  __| 
**     _ \   |  |   /   _ \   |   |  | .  |  _|  
**   _/  _\ \__/ _|_\ _/  _\ _|  \__/ _|\_| ___| 
**
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**      OpenCL binding (and more) for Scala
**
**         http://www.hsyl20.fr/auratune
**
*/

package fr.hsyl20.auratune

import fr.hsyl20.{opencl => cl}

class Codelet(val name:String, val source:String) {

   def program(context:cl.Context) = new cl.Program(context, source)

   def kernel(context:cl.Context) = new cl.Kernel(program(context), name)
}
