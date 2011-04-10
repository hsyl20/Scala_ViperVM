/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**         http://www.hsyl20.fr/vipervm             **
**                     GPLv3                        **
\*                                                  */

package fr.hsyl20.vipervm.platform

/**
 * Kernel parameter
 */
sealed abstract class KernelParameter
case class BufferKernelParameter(buffer:Buffer) extends KernelParameter
case class IntKernelParameter(value:Int)        extends KernelParameter
case class DoubleKernelParameter(value:Double)  extends KernelParameter
case class FloatKernelParameter(value:Float)    extends KernelParameter
