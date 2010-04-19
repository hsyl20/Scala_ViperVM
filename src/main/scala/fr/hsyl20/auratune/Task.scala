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
**                     GPLv3
*/

package fr.hsyl20.auratune

import scala.collection.immutable._

object Argument {
   type AccessMode = AccessMode.Value

   object AccessMode extends Enumeration {
      val In, Out, InOut = Value
   }
}

case class Argument(data:Data, access:Argument.AccessMode)

/**
 * A task ready to be executed. This class contains references to codelet arguments
 * and various parameters (group size...)
 */
class Task(val codelet:Codelet, val globalWorkSize:Seq[Long], val localWorkSize:Option[Seq[Long]]) {
   var args: List[Argument] = Nil
}
