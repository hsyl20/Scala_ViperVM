/*                                                  *\
** \ \     / _)                   \ \     /   \  |  **
**  \ \   /   |  __ \    _ \   __| \ \   /   |\/ |  **
**   \ \ /    |  |   |   __/  |     \ \ /    |   |  **
**    \_/    _|  .__/  \___| _|      \_/    _|  _|  **
**              _|                                  **
**                                                  **
**       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~          **
**                                                  **
**            http://www.vipervm.org                **
**                     GPLv3                        **
\*                                                  */

package org.vipervm.platform

/**
 * Kernel parameter
 */
sealed abstract class KernelParameter
case class BufferKernelParameter(buffer:Buffer) extends KernelParameter
case class IntKernelParameter(value:Int)        extends KernelParameter
case class LongKernelParameter(value:Long)      extends KernelParameter
case class DoubleKernelParameter(value:Double)  extends KernelParameter
case class FloatKernelParameter(value:Float)    extends KernelParameter
