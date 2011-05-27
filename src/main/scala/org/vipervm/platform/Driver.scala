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

/** Drivers give access to some devices (CUDA, OpenCL, CELL...)
 *
 * They are to be registered into a platform to be used
 */
abstract class Driver {

  /** Memory nodes */
  def memories:Seq[MemoryNode]

  /** Networks */
  def networks:Seq[Network]

  /** Processors */
  def processors:Seq[Processor]
}
