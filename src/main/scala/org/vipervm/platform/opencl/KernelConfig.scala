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

/**
 * Configuration for a kernel execution
 *
 * @param localWorkSize Local work size is optional. Default behavior is to let the OpenCL
 * implementation decide how to break the global work-items into work-groups
 */
case class OpenCLKernelConfig(
  kernelName:String,
  parameters: IndexedSeq[OpenCLKernelParameter],
  globalWorkSize: List[Long],
  localWorkSize: Option[List[Long]] = None 
)
