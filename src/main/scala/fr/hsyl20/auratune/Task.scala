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

/**
 * A task ready to be executed. This class contains references to codelet arguments
 * and various parameters (group size...)
 */
class Task(val kernel:Kernel, val inputs:Map[Symbol,Data], val outputs:Map[Symbol,Data], val globalWorkSize:Seq[Long], val localWorkSize:Option[Seq[Long]])
