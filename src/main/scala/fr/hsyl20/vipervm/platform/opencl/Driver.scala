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

package fr.hsyl20.vipervm.platform.opencl

import fr.hsyl20.vipervm.platform._
import fr.hsyl20.{opencl => cl}

/** OpenCL Driver
 */
class OpenCLDriver extends Driver {
   
  val processors:Seq[OpenCLProcessor] = cl.OpenCL.platforms.flatMap(_.devices()).map(new OpenCLProcessor(_))

  val networks:Seq[OpenCLNetwork] = processors.map(new OpenCLNetwork(_))

  val memories:Seq[OpenCLMemoryNode] = processors.map(new OpenCLMemoryNode(_))
}
