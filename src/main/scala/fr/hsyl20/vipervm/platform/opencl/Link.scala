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

import fr.hsyl20.vipervm.platform.{HostMemoryNode,Link}

case class OpenCLSourceLink(
  network:OpenCLNetwork,
  source:OpenCLMemoryNode,
  target:HostMemoryNode) extends Link

case class OpenCLTargetLink(
  network:OpenCLNetwork,
  source:HostMemoryNode,
  target:OpenCLMemoryNode) extends Link
