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

/**
 * Task parameter
 */
sealed abstract class TaskParameter {
  def toKernelParameter(mem:MemoryNode): KernelParameter = this match {
    case DataTaskParameter(d) => BufferKernelParameter(d.getBuffer(mem).get)
    case IntTaskParameter(v) => IntKernelParameter(v)
    case DoubleTaskParameter(v) => DoubleKernelParameter(v)
    case FloatTaskParameter(v) => FloatKernelParameter(v)
  }
}

case class DataTaskParameter(data:Data)       extends TaskParameter
case class IntTaskParameter(value:Int)        extends TaskParameter
case class DoubleTaskParameter(value:Double)  extends TaskParameter
case class FloatTaskParameter(value:Float)    extends TaskParameter

