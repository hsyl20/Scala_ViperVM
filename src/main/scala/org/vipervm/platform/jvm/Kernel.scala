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

package org.vipervm.platform.jvm

import org.vipervm.platform._

/**
 * Kernel that can be executed on the JVM (Scala,Java,Groovy...)
 */

abstract class JVMKernel extends Kernel {
  
  def canExecuteOn(proc:Processor): Boolean = proc.isInstanceOf[JVMProcessor]

  implicit def buf2buf(b:Buffer):HostBuffer = b.asInstanceOf[HostBuffer]

  def fun(args:Seq[Any]):Unit
}
