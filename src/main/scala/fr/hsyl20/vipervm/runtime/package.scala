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

package fr.hsyl20.vipervm

import fr.hsyl20.vipervm.platform._

package object runtime {

  implicit def bufferWrapper(buffer:Buffer) = new BufferWrapper(buffer)

}

/**
 * Add methods to Buffer
 */
class BufferWrapper(buffer:Buffer) {
  /** Get a 1D view */
  def view1D(offset:Long, size:Long):BufferView1D = {
    if (offset + size > buffer.size)
      throw new Exception("Invalid view: view out of bound (%d)".format(offset+size))

    BufferView1D(buffer,offset,size)
  }

  /** Get a 2D view */
  def view2D(offset:Long,width:Long,height:Long,rowPadding:Long):BufferView2D = {
    val sizeb = (width+rowPadding)*height
    if (sizeb + offset > buffer.size)
      throw new Exception("Invalid 2D view")
    
    BufferView2D(buffer,offset,width,height,rowPadding)
  }

  /** Get a 3D view */
  def view3D(offset:Long,width:Long,height:Long,depth:Long,rowPadding:Long,planePadding:Long):BufferView3D = {
    val sizeb = ((width+rowPadding)*height + planePadding)*depth
    if (sizeb + offset > buffer.size)
      throw new Exception("Invalid 3D view")
    
    BufferView3D(buffer,offset,width,height,depth,rowPadding,planePadding)
  }
}
