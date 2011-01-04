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

package fr.hsyl20.vipervm.runtime

/**
 * Matrix data type
 *
 * Matrix uses memory node natural padding
 */
class Matrix(dims:Seq[Long])(runtime:Runtime) extends Data {
  
  def sizeOn(memoryNode:MemoryNode): Long = {
    //TODO: use natural word size
    //(0L /: dims)((a,b) => a * memoryNode.padding(b))
    (0L /: dims)((a,b) => a * b)
  }

  def allocate(memoryNode:MemoryNode): Boolean = {
    val b = memoryNode.allocate(sizeOn(memoryNode))
    addBuffer(b)
    true
  }
}

object Matrix {
  
  /** 
   * Allocate a new matrix
   */
  def allocate(dims:Long*)(implicit runtime:Runtime) = {
    val m = new Matrix(dims)(runtime)
    runtime.allocate(m)
    m
  }

}
