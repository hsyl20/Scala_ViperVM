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
 * Describe a set of memory cells (not necessarily contiguous)
 *
 * Views can be used for data transfers and as kernel parameters
 */
sealed abstract class BufferView {
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
