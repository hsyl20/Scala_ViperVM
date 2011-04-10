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
 * A network between different memory nodes
 *
 * Network should at least support 1D data copy
 */
abstract class Network {
  /**
   * Return a link from source to target using this network if possible
   */
  def link(source:MemoryNode,target:MemoryNode): Option[Link]

  /**
   * Return a MemoryCopier that uses this network
   */
  def memoryCopier: MemoryCopier
}

