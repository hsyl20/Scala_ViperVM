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

package org.vipervm.runtime

/*/**
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
*/
