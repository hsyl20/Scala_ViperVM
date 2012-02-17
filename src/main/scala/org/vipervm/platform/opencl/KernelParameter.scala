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

package org.vipervm.platform.opencl

sealed abstract class OpenCLKernelParameter
case class OpenCLBufferKernelParameter(buffer:OpenCLBuffer) extends OpenCLKernelParameter
case class OpenCLIntKernelParameter(value:Int)        extends OpenCLKernelParameter
case class OpenCLLongKernelParameter(value:Long)      extends OpenCLKernelParameter
case class OpenCLDoubleKernelParameter(value:Double)  extends OpenCLKernelParameter
case class OpenCLFloatKernelParameter(value:Float)    extends OpenCLKernelParameter
