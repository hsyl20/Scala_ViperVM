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
 * Types supported for kernel parameters
 */
sealed abstract class KernelParameterType
case object BufferParameterType extends KernelParameterType
case object IntParameterType  extends KernelParameterType
case object DoubleParameterType extends KernelParameterType
case object FloatParameterType  extends KernelParameterType
