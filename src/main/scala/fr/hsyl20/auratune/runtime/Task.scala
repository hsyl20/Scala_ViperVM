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

package fr.hsyl20.auratune.runtime

import fr.hsyl20.auratune.runtime.AccessMode._

/**
 * A task ready to be scheduled.
 */
case class Task(kernels:KernelSet, args:List[(TaskParameter,AccessMode)]) {
  
  /**
   * Return Data parameters
   */
  def data: List[(Data,AccessMode)] = args.flatMap(a => a._1 match {
    case DataTaskParameter(d) => Some((d,a._2))
    case _ => None
  })
}
