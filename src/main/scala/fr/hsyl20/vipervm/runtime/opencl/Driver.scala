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

package fr.hsyl20.vipervm.runtime.opencl

import fr.hsyl20.vipervm.runtime._
import fr.hsyl20.{opencl => cl}

/** OpenCL Driver
 */
class OpenCLDriver extends Driver {
   
  def processors:Seq[OpenCLDevice] = for (p <- cl.OpenCL.platforms ; peer <- p.devices())
    yield new OpenCLDevice(peer)

  def networks:Seq[OpenCLNetwork] = for (p <- processors) yield new OpenCLNetwork(p)

  def memoryNodes:Seq[OpenCLMemoryNode] = for (p <- processors) yield new OpenCLMemoryNode(p)
}
