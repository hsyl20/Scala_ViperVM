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

/** Drivers give access to some devices (CUDA, OpenCL, CELL...)
 *
 * They are to be registered into a platform to be used
 */
abstract class Driver {

  /** Memory nodes */
  def memoryNodes:Seq[MemoryNode]

  /** Networks */
  def networks:Seq[Network]

  /** Processors */
  def processors:Seq[Processor]
}
