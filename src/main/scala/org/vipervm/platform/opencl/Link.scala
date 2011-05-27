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

package org.vipervm.platform.opencl

import org.vipervm.platform.{HostMemoryNode,Link}

case class OpenCLReadLink(
  network:OpenCLNetwork,
  source:OpenCLMemoryNode,
  target:HostMemoryNode) extends Link

case class OpenCLWriteLink(
  network:OpenCLNetwork,
  source:HostMemoryNode,
  target:OpenCLMemoryNode) extends Link
