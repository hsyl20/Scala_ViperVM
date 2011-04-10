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
 * A view of a buffer
 *
 * View can be used for data transfers and as kernel parameters
 */
trait BufferView {
  /** Associated buffer */
  val buffer:Buffer

  /** Offset in the buffer */
  val offset:Long
}

case class BufferView1D(buffer:Buffer,offset:Long,size:Long) extends BufferView
case class BufferView2D(buffer:Buffer,offset:Long,width:Long,height:Long,rowPadding:Long) extends BufferView
case class BufferView3D(buffer:Buffer,offset:Long,width:Long,height:Long,depth:Long,rowPadding:Long,planePadding:Long) extends BufferView
