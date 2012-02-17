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

package org.vipervm.platform.jvm

sealed abstract class JVMKernelParameter
case class JVMBufferKernelParameter(buffer:JVMBuffer) extends JVMKernelParameter
case class JVMIntKernelParameter(value:Int)        extends JVMKernelParameter
case class JVMLongKernelParameter(value:Long)      extends JVMKernelParameter
case class JVMDoubleKernelParameter(value:Double)  extends JVMKernelParameter
case class JVMFloatKernelParameter(value:Float)    extends JVMKernelParameter

