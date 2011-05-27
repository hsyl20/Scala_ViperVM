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

import org.vipervm.platform.Buffer
import org.vipervm.bindings.{opencl => cl}
import com.sun.jna.Memory

/**
 * OpenCL buffer
 */
class OpenCLBuffer(val size:Long, val peer:cl.Buffer, val memory:OpenCLMemoryNode) extends Buffer
