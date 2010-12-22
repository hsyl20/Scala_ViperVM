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
 * Kernel parameter
 */
sealed abstract class KernelParameter
case class BufferKernelParameter(buffer:Buffer) extends KernelParameter
case class IntKernelParameter(value:Int)        extends KernelParameter
case class DoubleKernelParameter(value:Double)  extends KernelParameter
case class FloatKernelParameter(value:Float)    extends KernelParameter
