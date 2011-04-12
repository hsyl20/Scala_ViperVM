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
 * Describe a set of memory cells (not necessarily contiguous)
 *
 * Views can be used for data transfers and as kernel parameters
 */
trait BufferView {
  /** Associated buffer */
  val buffer:Buffer

  /** Offset in the buffer */
  val offset:Long
}

/** Contiguous view */
case class BufferView1D(buffer:Buffer,offset:Long,size:Long) extends BufferView

/** 2D view with row padding */
case class BufferView2D(buffer:Buffer,offset:Long,width:Long,height:Long,rowPadding:Long) extends BufferView

/** 2D view with row and plane paddings */
case class BufferView3D(buffer:Buffer,offset:Long,width:Long,height:Long,depth:Long,rowPadding:Long,planePadding:Long) extends BufferView
